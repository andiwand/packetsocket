package at.stefl.packetsocket.pdu;

public class UDPSegment extends PDU {
	
	private int sourcePort;
	private int destinationPort;
	private PDU payload;
	
	public int getSourcePort() {
		return sourcePort;
	}
	
	public int getDestinationPort() {
		return destinationPort;
	}
	
	public PDU getPayload() {
		return payload;
	}
	
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}
	
	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}
	
	public void setPayload(PDU payload) {
		this.payload = payload;
	}
	
}