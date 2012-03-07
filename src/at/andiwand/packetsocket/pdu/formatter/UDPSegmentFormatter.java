package at.andiwand.packetsocket.pdu.formatter;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.Assignments;
import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packetsocket.pdu.UDPSegment;


// TODO: ports?
public class UDPSegmentFormatter extends GenericPDUFormatter<UDPSegment> {
	
	private static final Map<Integer, PDUFormatter> PAYLOAD_FORMATTERS = new HashMap<Integer, PDUFormatter>();
	
	static {
		PAYLOAD_FORMATTERS.put(Assignments.UDP.PORT_DNS,
				new DNSPacketFormatter());
		PAYLOAD_FORMATTERS.put(Assignments.UDP.PORT_DHCP_SERVER,
				new DHCPPacketFormatter());
		PAYLOAD_FORMATTERS.put(Assignments.UDP.PORT_DHCP_CLIENT,
				new DHCPPacketFormatter());
	}
	
	@Override
	protected void formatGeneric(UDPSegment segment,
			ExtendedDataOutputStream out) {
		ExtendedDataOutputStream segmentOut = new ExtendedDataOutputStream();
		
		segmentOut.writeShort(segment.getSourcePort() & 0xffff);
		segmentOut.writeShort(segment.getDestinationPort() & 0xffff);
		segmentOut.writeShort(0); // length
		segmentOut.writeShort(0); // checksum
		
		int minPort = Math.min(segment.getSourcePort(), segment
				.getDestinationPort());
		PDUFormatter payloadFormatter = PAYLOAD_FORMATTERS.get(minPort);
		payloadFormatter.format(segment.getPayload(), segmentOut);
		
		byte[] bytes = segmentOut.getData();
		int length = bytes.length;
		bytes[4] = (byte) (length >> 8);
		bytes[5] = (byte) (length >> 0);
		
		out.write(bytes);
	}
	
	@Override
	public UDPSegment parse(ExtendedDataInputStream in) {
		UDPSegment segment = new UDPSegment();
		
		segment.setSourcePort(in.readUnsignedShort());
		segment.setDestinationPort(in.readUnsignedShort());
		int length = in.readShort(); // length
		in.readShort(); // checksum
		
		byte[] payloadBuffer = new byte[length - 8];
		in.read(payloadBuffer);
		ExtendedDataInputStream payloadInputStream = new ExtendedDataInputStream(
				payloadBuffer);
		
		int minPort = Math.min(segment.getSourcePort(), segment
				.getDestinationPort());
		PDUFormatter payloadFormatter = PAYLOAD_FORMATTERS.get(minPort);
		if (payloadFormatter == null)
			throw new IllegalArgumentException("Unsupported port!");
		PDU payload = payloadFormatter.parse(payloadInputStream);
		segment.setPayload(payload);
		
		return segment;
	}
	
}