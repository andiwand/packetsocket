package at.andiwand.packetsocket.pdu;

import at.andiwand.library.network.ip.IPv4Address;


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
	
	public void setVersion(byte version) {
		this.version = version;
	}
	
	public void setTypeOfService(byte typeOfService) {
		this.typeOfService = typeOfService;
	}
	
	public void setIdentication(int identication) {
		this.identication = identication;
	}
	
	public void setFlags(byte flags) {
		this.flags = flags;
	}
	
	public void setFragmentOffset(short fragmentOffset) {
		this.fragmentOffset = fragmentOffset;
	}
	
	public void setTimeToLive(short timeToLive) {
		this.timeToLive = timeToLive;
	}
	
	public void setProtocol(byte protocol) {
		this.protocol = protocol;
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