package at.andiwand.packetsocket.pdu.formatter;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.InternetChecksum;
import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.ICMPPacket;


public class ICMPPacketFormatter extends GenericPDUFormatter<ICMPPacket> {
	
	@Override
	protected void formatGeneric(ICMPPacket packet,
			ExtendedDataOutputStream outputStream) {
		ExtendedDataOutputStream packetWriter = new ExtendedDataOutputStream();
		
		packetWriter.writeByte(packet.getType());
		packetWriter.writeByte(packet.getCode());
		packetWriter.writeShort(0); // checksum
		
		ICMPPacket.ICMPPayload payload = packet.getPayload();
		
		switch (packet.getType()) {
		case Assignments.ICMP.TYPE_ECHO:
		case Assignments.ICMP.TYPE_ECHO_REPLY:
			ICMPPacket.EchoPayload echoPayload = (ICMPPacket.EchoPayload) payload;
			packetWriter.writeShort(echoPayload.getIdentifier());
			packetWriter.writeShort(echoPayload.getSequenceNumber());
			packetWriter.write(echoPayload.getData());
			break;
		default:
			throw new IllegalStateException("Unreachable section!");
		}
		
		byte[] bytes = packetWriter.getData();
		short checksum = InternetChecksum.calculateChecksum(bytes, 0,
				bytes.length);
		bytes[2] = (byte) (checksum >> 8);
		bytes[3] = (byte) (checksum >> 0);
		
		outputStream.write(bytes);
	}
	
	@Override
	public ICMPPacket parse(ExtendedDataInputStream inputStream) {
		ICMPPacket packet = new ICMPPacket();
		
		packet.setType(inputStream.readByte());
		packet.setCode(inputStream.readByte());
		inputStream.readShort(); // checksum
		
		ICMPPacket.ICMPPayload payload;
		
		switch (packet.getCode()) {
		case Assignments.ICMP.TYPE_ECHO:
		case Assignments.ICMP.TYPE_ECHO_REPLY:
			ICMPPacket.EchoPayload echoPayload = new ICMPPacket.EchoPayload();
			echoPayload.setIdentifier(inputStream.readUnsignedShort());
			echoPayload.setSequenceNumber(inputStream.readUnsignedShort());
			echoPayload.setData(inputStream.readBytes());
			payload = echoPayload;
			break;
		default:
			throw new IllegalStateException("Unsupported type!");
		}
		
		packet.setPayload(payload);
		
		return packet;
	}
	
}