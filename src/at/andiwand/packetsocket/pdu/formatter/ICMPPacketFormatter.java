package at.andiwand.packetsocket.pdu.formatter;

import java.io.ByteArrayOutputStream;

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
		
		switch (packet.getType()) {
		case Assignments.ICMP.TYPE_ECHO:
		case Assignments.ICMP.TYPE_ECHO_REPLY:
			ICMPPacket.Echo echoPacket = (ICMPPacket.Echo) packet;
			packetWriter.writeShort(echoPacket.getIdentifier());
			packetWriter.writeShort(echoPacket.getSequenceNumber());
			packetWriter.write(echoPacket.getData());
			break;
		default:
			throw new IllegalStateException("Unreachable section");
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
		ICMPPacket packet;
		
		byte type = inputStream.readByte();
		byte code = inputStream.readByte();
		inputStream.readShort(); // checksum
		
		switch (type) {
		case Assignments.ICMP.TYPE_ECHO:
		case Assignments.ICMP.TYPE_ECHO_REPLY:
			ICMPPacket.Echo echoPacket = new ICMPPacket.Echo();
			
			echoPacket.setIdentifier(inputStream.readUnsignedShort());
			echoPacket.setSequenceNumber(inputStream.readUnsignedShort());
			
			ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
			
			int read;
			while ((read = inputStream.read()) != -1)
				dataOutputStream.write(read);
			
			echoPacket.setData(dataOutputStream.toByteArray());
			
			packet = echoPacket;
			break;
		default:
			throw new IllegalStateException("Unsupported type!");
		}
		
		packet.setType(type);
		packet.setCode(code);
		
		return packet;
	}
	
}