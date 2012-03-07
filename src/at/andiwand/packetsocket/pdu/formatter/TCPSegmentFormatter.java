package at.andiwand.packetsocket.pdu.formatter;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.Assignments;
import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packetsocket.pdu.TCPSegment;


// TODO: ports?
// TODO: implement options
public class TCPSegmentFormatter extends GenericPDUFormatter<TCPSegment> {
	
	private static final Map<Integer, PDUFormatter> PAYLOAD_FORMATTERS = new HashMap<Integer, PDUFormatter>();
	
	static {
		PAYLOAD_FORMATTERS.put(Assignments.TCP.PORT_TELNET,
				new TelnetSegmentFormatter());
	}
	
	@Override
	protected void formatGeneric(TCPSegment segment,
			ExtendedDataOutputStream out) {
		out.writeShort(segment.getSourcePort() & 0xffff);
		out.writeShort(segment.getDestinationPort() & 0xffff);
		out.writeInt((int) (segment.getSequenceNumber() & 0xffffffff));
		out.writeInt((int) (segment.getAcknowledgmentNumber() & 0xffffffff));
		int tmp = 0;
		tmp |= (segment.getDataOffset() & 0x0f) << 28;
		tmp |= (segment.getReserved() & 0x3f) << 22;
		tmp |= (segment.getFlags() & 0x3f) << 16;
		tmp |= (segment.getWindow() & 0xffff) << 0;
		out.writeInt(tmp);
		out.writeShort(segment.getChecksum());
		out.writeShort(segment.getUrgentPointer() & 0xffff);
		
		if (segment.getPayload() != null) {
			int minPort = Math.min(segment.getSourcePort(),
					segment.getDestinationPort());
			PDUFormatter payloadFormatter = PAYLOAD_FORMATTERS.get(minPort);
			payloadFormatter.format(segment.getPayload(), out);
		}
	}
	
	@Override
	public TCPSegment parse(ExtendedDataInputStream in) {
		TCPSegment segment = new TCPSegment();
		
		segment.setSourcePort(in.readShort() & 0xffff);
		segment.setDestinationPort(in.readShort() & 0xffff);
		segment.setSequenceNumber(in.readInt() & 0xffffffff);
		segment.setAcknowledgmentNumber(in.readInt() & 0xffffffff);
		int tmp = in.readInt();
		int dataOffset = (tmp >> 28) & 0x0f;
		segment.setDataOffset((byte) dataOffset);
		segment.setReserved((byte) ((tmp >> 22) & 0x3f));
		segment.setFlags((byte) ((tmp >> 16) & 0x3f));
		segment.setWindow((tmp >> 0) & 0xffff);
		segment.setChecksum(in.readShort());
		segment.setUrgentPointer(in.readShort() & 0xffff);
		
		for (int i = 5; i < dataOffset; i++) {
			in.readInt();
		}
		
		// TODO: payload?
		int minPort = Math.min(segment.getSourcePort(),
				segment.getDestinationPort());
		PDUFormatter payloadFormatter = PAYLOAD_FORMATTERS.get(minPort);
		PDU payload = payloadFormatter.parse(in);
		segment.setPayload(payload);
		
		return segment;
	}
	
}