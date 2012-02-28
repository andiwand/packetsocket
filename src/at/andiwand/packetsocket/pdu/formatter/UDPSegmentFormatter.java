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
		PAYLOAD_FORMATTERS
				.put(Assignments.DHCP.PORT, new DHCPPacketFormatter());
	}
	
	@Override
	protected void formatGeneric(UDPSegment segment,
			ExtendedDataOutputStream out) {
		ExtendedDataOutputStream segmentOut = new ExtendedDataOutputStream();
		
		segmentOut.writeShort(segment.getSourcePort());
		segmentOut.writeShort(segment.getDestinationPort());
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
		
		segment.setSourcePort(in.readShort());
		segment.setDestinationPort(in.readShort());
		in.readShort(); // length
		in.readShort(); // checksum
		
		int minPort = Math.min(segment.getSourcePort(), segment
				.getDestinationPort());
		PDUFormatter payloadFormatter = PAYLOAD_FORMATTERS.get(minPort);
		PDU payload = payloadFormatter.parse(in);
		segment.setPayload(payload);
		
		return segment;
	}
	
}