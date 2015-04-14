package at.stefl.packetsocket.pdu;

import at.stefl.commons.network.ip.IPv4Address;


// TODO: add length fields
public class IPv4Packet extends PDU {
	
	private byte version;
	private byte typeOfService;
	private int identication;
	private byte flags;
	private short fragmentOffset;
	private short timeToLive;
	private byte protocol;
	private IPv4Address source;
	private IPv4Address destination;
	private PDU payload;
	
	public byte getVersion() {
		return version;
	}
	
	public byte getTypeOfService() {
		return typeOfService;
	}
	
	public int getIdentication() {
		return identication;
	}
	
	public byte getFlags() {
		return flags;
	}
	
	public short getFragmentOffset() {
		return fragmentOffset;
	}
	
	public short getTimeToLive() {
		return timeToLive;
	}
	
	public byte getProtocol() {
		return protocol;
	}
	
	public IPv4Address getSource() {
		return source;
	}
	
	public IPv4Address getDestination() {
		return destination;
	}
	
	public PDU getPayload() {
		return payload;
	}
	
	public void setVersion(int version) {
		this.version = (byte) version;
	}
	
	public void setTypeOfService(int typeOfService) {
		this.typeOfService = (byte) typeOfService;
	}
	
	public void setIdentication(int identication) {
		this.identication = identication;
	}
	
	public void setFlags(int flags) {
		this.flags = (byte) flags;
	}
	
	public void setFragmentOffset(int fragmentOffset) {
		this.fragmentOffset = (short) fragmentOffset;
	}
	
	public void setTimeToLive(int timeToLive) {
		this.timeToLive = (short) timeToLive;
	}
	
	public void setProtocol(int protocol) {
		this.protocol = (byte) protocol;
	}
	
	public void setSource(IPv4Address source) {
		this.source = source;
	}
	
	public void setDestination(IPv4Address destination) {
		this.destination = destination;
	}
	
	public void setPayload(PDU payload) {
		this.payload = payload;
	}
	
}