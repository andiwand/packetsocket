package at.andiwand.packetsocket.pdu;

import at.andiwand.library.network.mac.MACAddress;


public class Ethernet2Frame extends PDU {
	
	private MACAddress destination;
	private MACAddress source;
	private short type;
	private PDU payload;
	
	public MACAddress getDestination() {
		return destination;
	}
	
	public MACAddress getSource() {
		return source;
	}
	
	public short getType() {
		return type;
	}
	
	public PDU getPayload() {
		return payload;
	}
	
	public void setDestination(MACAddress destination) {
		this.destination = destination;
	}
	
	public void setSource(MACAddress source) {
		this.source = source;
	}
	
	public void setType(int type) {
		this.type = (short) type;
	}
	
	public void setPayload(PDU payload) {
		this.payload = payload;
	}
	
}