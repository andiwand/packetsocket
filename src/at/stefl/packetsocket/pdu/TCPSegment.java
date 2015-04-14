package at.stefl.packetsocket.pdu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// TODO: implement options
public class TCPSegment extends PDU {
	
	public static abstract class Option {
		private byte option;
		
		public byte getOption() {
			return option;
		}
		
		public void setOption(byte option) {
			this.option = option;
		}
	}
	
	public static class EndOption extends Option {}
	
	public static class NoOption extends Option {}
	
	public static class MaximumSegmentSizeOption extends Option {
		private int maximumSegmentSize;
		
		public int getMaximumSegmentSize() {
			return maximumSegmentSize;
		}
		
		public void setMaximumSegmentSize(int maximumSegmentSize) {
			this.maximumSegmentSize = maximumSegmentSize;
		}
	}
	
	public static class WindowScaleOption extends Option {
		private int shiftCnt;
		
		public int getShiftCnt() {
			return shiftCnt;
		}
		
		public void setShiftCnt(int shiftCnt) {
			this.shiftCnt = shiftCnt;
		}
	}
	
	public static class SackPermittedOption extends Option {}
	
	public static class TimestampOption extends Option {
		private long value;
		private long echoReply;
		
		public long getValue() {
			return value;
		}
		
		public long getEchoReply() {
			return echoReply;
		}
		
		public void setValue(long value) {
			this.value = value;
		}
		
		public void setEchoReply(long echoReply) {
			this.echoReply = echoReply;
		}
	}
	
	public static class WindowScale extends Option {
		
	}
	
	private int sourcePort;
	private int destinationPort;
	private long sequenceNumber;
	private long acknowledgmentNumber;
	private byte reserved;
	private byte flags;
	private int window;
	private short checksum;
	private int urgentPointer;
	private List<Option> options = new ArrayList<Option>();
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
	
	public List<Option> getOptions() {
		return Collections.unmodifiableList(options);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Option> T getOption(Class<T> optionClass) {
		for (Option option : options) {
			if (option.getClass().equals(optionClass)) return (T) option;
		}
		
		return null;
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
	
	public void setOptions(List<Option> options) {
		this.options = new ArrayList<Option>(options);
	}
	
	public void setPayload(PDU payload) {
		this.payload = payload;
	}
	
	public void addOption(Option option) {
		options.add(option);
	}
	
	public void removeOption(Option option) {
		options.remove(option);
	}
	
}