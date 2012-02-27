package at.andiwand.packetsocket.pdu.formatter;

import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.UDPSegment;


public class UDPSegmentFormatter extends GenericPDUFormatter<UDPSegment> {
	
	@Override
	protected void formatGeneric(UDPSegment segment,
			ExtendedDataOutputStream outputStream) {
		ExtendedDataOutputStream segmentOutputStream = new ExtendedDataOutputStream();
		
		segmentOutputStream.writeShort(segment.getSourcePort());
		segmentOutputStream.writeShort(segment.getDestinationPort());
		segmentOutputStream.writeShort(0); // length
		segmentOutputStream.writeShort(0); // checksum
		
		byte[] bytes = segmentOutputStream.getData();
		int length = bytes.length;
		bytes[4] = (byte) (length >> 8);
		bytes[5] = (byte) (length >> 0);
		
		outputStream.write(bytes);
	}
	
	@Override
	public UDPSegment parse(ExtendedDataInputStream inputStream) {
		UDPSegment segment = new UDPSegment();
		
		segment.setSourcePort(inputStream.readShort());
		segment.setDestinationPort(inputStream.readShort());
		inputStream.readShort(); // length
		inputStream.readShort(); // checksum
		
		return segment;
	}
	
}