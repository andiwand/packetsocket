package at.andiwand.packetsocket.pdu.formatter;

import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.TCPSegment;


public class TCPSegmentFormatter extends GenericPDUFormatter<TCPSegment> {
	
	@Override
	protected void formatGeneric(TCPSegment segment,
			ExtendedDataOutputStream outputStream) {
		
	}
	
	@Override
	public TCPSegment parse(ExtendedDataInputStream inputStream) {
		TCPSegment packet = new TCPSegment();
		
		return packet;
	}
	
}