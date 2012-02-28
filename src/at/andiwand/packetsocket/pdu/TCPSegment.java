package at.andiwand.packetsocket.pdu;

// TODO: implement options
public class TCPSegment extends PDU {
	
	private int sourcePort;
	private int destinationPort;
	private long sequenceNumber;
	private long acknowledgmentNumber;
	private byte dataOffset;
	private byte reserved;
	private byte flags;
	private int window;
	private short checksum;
	private int urgentPointer;
	private PDU payload;
	
	public int getSourcePort() {
		return sourcePort;
	}
	
	public int getDestinationPort() {
		return destinationPort;
	}
	
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	
	public long getAcknowledgmentNumber() {
		return acknowledgmentNumber;
	}
	
	public byte getDataOffset() {
		return dataOffset;
	}
	
	public byte getReserved() {
		return reserved;
	}
	
	public byte getFlags() {
		return flags;
	}
	
	public int getWindow() {
		return window;
	}
	
	public short getChecksum() {
		return checksum;
	}
	
	public int getUrgentPointer() {
		return urgentPointer;
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
	
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public void setAcknowledgmentNumber(long acknowledgmentNumber) {
		this.acknowledgmentNumber = acknowledgmentNumber;
	}
	
	public void setDataOffset(byte dataOffset) {
		this.dataOffset = dataOffset;
	}
	
	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}
	
	public void setFlags(byte flags) {
		this.flags = flags;
	}
	
	public void setWindow(int window) {
		this.window = window;
	}
	
	public void setChecksum(short checksum) {
		this.checksum = checksum;
	}
	
	public void setUrgentPointer(int urgentPointer) {
		this.urgentPointer = urgentPointer;
	}
	
	public void setPayload(PDU payload) {
		this.payload = payload;
	}
	
}