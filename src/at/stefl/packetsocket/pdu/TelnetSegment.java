package at.stefl.packetsocket.pdu;

import java.util.Arrays;


public class TelnetSegment extends PDU {
	
	private byte[] data;
	
	public byte[] getData() {
		return Arrays.copyOf(data, data.length);
	}
	
	public void setData(byte[] data) {
		this.data = Arrays.copyOf(data, data.length);
	}
	
}