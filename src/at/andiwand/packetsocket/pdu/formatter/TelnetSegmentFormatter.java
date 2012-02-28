package at.andiwand.packetsocket.pdu.formatter;

import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.TelnetSegment;


public class TelnetSegmentFormatter extends GenericPDUFormatter<TelnetSegment> {
	
	@Override
	protected void formatGeneric(TelnetSegment pdu, ExtendedDataOutputStream out) {
		out.write(pdu.getData());
	}
	
	@Override
	public TelnetSegment parse(ExtendedDataInputStream in) {
		TelnetSegment segment = new TelnetSegment();
		
		segment.setData(in.readBytes());
		
		return segment;
	}
	
}