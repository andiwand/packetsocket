package at.andiwand.packetsocket.pdu;

// TODO: implement icmp message types
public class ICMPPacket extends PDU {
	
	public static abstract class ICMPPayload extends PDU {}
	
	public static class EchoPayload extends ICMPPayload {
		private int identifier;
		private int sequenceNumber;
		private byte[] data;
		
		public int getIdentifier() {
			return identifier;
		}
		
		public int getSequenceNumber() {
			return sequenceNumber;
		}
		
		public byte[] getData() {
			return data;
		}
		
		public void setIdentifier(int identifier) {
			this.identifier = identifier;
		}
		
		public void setSequenceNumber(int sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}
		
		public void setData(byte[] data) {
			this.data = data;
		}
	}
	
	private byte type;
	private byte code;
	private ICMPPayload payload;
	
	public byte getType() {
		return type;
	}
	
	public byte getCode() {
		return code;
	}
	
	public ICMPPayload getPayload() {
		return payload;
	}
	
	public void setType(byte type) {
		this.type = type;
	}
	
	public void setCode(byte code) {
		this.code = code;
	}
	
	public void setPayload(ICMPPayload payload) {
		this.payload = payload;
	}
	
}