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
		if (result == null) throw new IllegalArgumentException(
				"Unregistered protocol");
		return result;
	}
	
	@Override
	protected void formatGeneric(IPv4Packet packet, ExtendedDataOutputStream out) {
		ExtendedDataOutputStream packetOut = new ExtendedDataOutputStream();
		
		int versionIHL = (packet.getVersion() & 0x0f) << 4;
		packetOut.writeByte(versionIHL);
		packetOut.writeByte(packet.getTypeOfService());
		packetOut.writeShort(0); // total length
		packetOut.writeShort(packet.getIdentication());
		int flagsFragmentOffset = ((packet.getFlags() & 0x07) << 13)
				| ((0 & 0x1fff) << 0);
		packetOut.writeShort(flagsFragmentOffset);
		packetOut.writeByte(packet.getTimeToLive());
		packetOut.writeByte(packet.getProtocol());
		packetOut.writeShort(0); // checksum
		packetOut.writeIPv4Address(packet.getSource());
		packetOut.writeIPv4Address(packet.getDestination());
		
		int headerSize = packetOut.size();
		int ihl = headerSize >> 2;
		
		PDUFormatter payloadFormatter = getPayloadFormatter(packet.getProtocol());
		payloadFormatter.format(packet.getPayload(), packetOut);
		
		byte[] bytes = packetOut.getData();
		int totalLength = bytes.length;
		
		bytes[0] |= ihl & 0x0f;
		bytes[2] = (byte) (totalLength >> 8);
		bytes[3] = (byte) (totalLength >> 0);
		
		short checksum = InternetChecksum.calculateChecksum(bytes, 0,
				headerSize);
		bytes[10] = (byte) (checksum >> 8);
		bytes[11] = (byte) (checksum >> 0);
		
		out.write(bytes);
	}
	
	public IPv4Packet parse(ExtendedDataInputStream in) {
		IPv4Packet packet = new IPv4Packet();
		
		byte versionIHL = in.readByte();
		packet.setVersion((byte) ((versionIHL >> 4) & 0x0f));
		int ihl = (versionIHL & 0x0f);
		packet.setTypeOfService(in.readByte());
		int totalLength = in.readUnsignedShort();
		packet.setIdentication(in.readUnsignedShort());
		short flagsFragmentOffset = in.readShort();
		packet.setFlags((byte) ((flagsFragmentOffset >> 13) & 0x07));
		int fragmentOffset = flagsFragmentOffset & 0x1fff;
		if (fragmentOffset != 0) throw new UnsupportedOperationException(
				"Unsupported fragment offset!");
		packet.setTimeToLive((short) in.readUnsignedByte());
		packet.setProtocol(in.readByte());
		in.readShort(); // checksum
		packet.setSource(in.readIPv4Address());
		packet.setDestination(in.readIPv4Address());
		
		// kill options
		for (int i = 5; i < ihl; i++) {
			in.readInt();
		}
		
		int payloadSize = totalLength - (ihl - 5) * 4;
		byte[] payloadBuffer = new byte[payloadSize];
		in.read(payloadBuffer);
		ExtendedDataInputStream payloadIn = new ExtendedDataInputStream(
				payloadBuffer);
		PDUFormatter payloadFormatter = getPayloadFormatter(packet.getProtocol());
		packet.setPayload(payloadFormatter.parse(payloadIn));
		
		return packet;
	}
	
}