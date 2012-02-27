package at.andiwand.packetsocket.pdu.formatter;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.InternetChecksum;
import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.IPv4Packet;


// TODO implement fragment offset, options
public class IPv4PacketFormatter extends GenericPDUFormatter<IPv4Packet> {
	
	private static final Map<Byte, PDUFormatter> PAYLOAD_FORMATTERS = new HashMap<Byte, PDUFormatter>();
	
	static {
		PAYLOAD_FORMATTERS.put(Assignments.IP.PROTOCOL_ICMP,
				new ICMPPacketFormatter());
		PAYLOAD_FORMATTERS.put(Assignments.IP.PROTOCOL_TCP,
				new TCPSegmentFormatter());
		PAYLOAD_FORMATTERS.put(Assignments.IP.PROTOCOL_UDP,
				new UDPSegmentFormatter());
	}
	
	private static PDUFormatter getPayloadFormatter(byte protocol) {
		PDUFormatter result = PAYLOAD_FORMATTERS.get(protocol);
		if (result == null)
			throw new IllegalArgumentException("Unregistered protocol");
		return result;
	}
	
	@Override
	protected void formatGeneric(IPv4Packet packet,
			ExtendedDataOutputStream outputStream) {
		ExtendedDataOutputStream packetOutputStream = new ExtendedDataOutputStream();
		
		int versionIHL = (packet.getVersion() & 0x0f) << 4;
		packetOutputStream.writeByte(versionIHL);
		packetOutputStream.writeByte(packet.getTypeOfService());
		packetOutputStream.writeShort(0); // total length
		packetOutputStream.writeShort(packet.getIdentication());
		int flagsFragmentOffset = ((packet.getFlags() & 0x07) << 13)
				| ((0 & 0x1fff) << 0);
		packetOutputStream.writeShort(flagsFragmentOffset);
		packetOutputStream.writeByte(packet.getTimeToLive());
		packetOutputStream.writeByte(packet.getProtocol());
		packetOutputStream.writeShort(0); // checksum
		packetOutputStream.writeIPv4Address(packet.getSource());
		packetOutputStream.writeIPv4Address(packet.getDestination());
		
		int headerSize = packetOutputStream.size();
		int ihl = headerSize >> 2;
		
		PDUFormatter payloadFormatter = getPayloadFormatter(packet
				.getProtocol());
		payloadFormatter.format(packet.getPayload(), packetOutputStream);
		
		byte[] bytes = packetOutputStream.getData();
		int totalLength = bytes.length;
		
		bytes[0] |= ihl & 0x0f;
		bytes[2] = (byte) (totalLength >> 8);
		bytes[3] = (byte) (totalLength >> 0);
		
		short checksum = InternetChecksum.calculateChecksum(bytes, 0,
				headerSize);
		bytes[10] = (byte) (checksum >> 8);
		bytes[11] = (byte) (checksum >> 0);
		
		outputStream.write(bytes);
	}
	
	public IPv4Packet parse(ExtendedDataInputStream inputStream) {
		IPv4Packet packet = new IPv4Packet();
		
		byte versionIHL = inputStream.readByte();
		packet.setVersion((byte) ((versionIHL >> 4) & 0x0f));
		int ihl = (versionIHL & 0x0f);
		packet.setTypeOfService(inputStream.readByte());
		inputStream.readUnsignedShort(); // total length
		packet.setIdentication(inputStream.readUnsignedShort());
		short flagsFragmentOffset = inputStream.readShort();
		packet.setFlags((byte) ((flagsFragmentOffset >> 13) & 0x07));
		int fragmentOffset = flagsFragmentOffset & 0x1fff;
		if (fragmentOffset != 0)
			throw new UnsupportedOperationException(
					"Unsupported fragment offset!");
		packet.setTimeToLive((short) inputStream.readUnsignedByte());
		packet.setProtocol(inputStream.readByte());
		inputStream.readShort(); // checksum
		packet.setSource(inputStream.readIPv4Address());
		packet.setDestination(inputStream.readIPv4Address());
		
		for (int i = 5; i < ihl; i++) {
			inputStream.readInt(); // kill options
		}
		
		PDUFormatter payloadFormatter = getPayloadFormatter(packet
				.getProtocol());
		packet.setPayload(payloadFormatter.parse(inputStream));
		
		return packet;
	}
	
}