package at.andiwand.packetsocket.pdu.formatter;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.ARPPacket;


public class ARPPacketFormatter extends GenericPDUFormatter<ARPPacket> {
	
	public static void writeHardwareAddress(Object address, short hardwareType,
			ExtendedDataOutputStream outputStream) {
		switch (hardwareType) {
		case Assignments.ARP.HARDWARE_TYPE_ETHERNET:
			MACAddress macAddress = (MACAddress) address;
			outputStream.writeMACAddress(macAddress);
			break;
		default:
			throw new IllegalStateException("Unsupported hardware type!");
		}
	}
	
	public static void writeProtocolAddress(Object address, short protocolType,
			ExtendedDataOutputStream outputStream) {
		switch (protocolType) {
		case Assignments.Ethernet.TYPE_IPV4:
			outputStream.writeIPv4Address((IPv4Address) address);
			break;
		default:
			throw new IllegalStateException("Unsupported protocol type!");
		}
	}
	
	public static Object readHardwareAddress(short hardwareType,
			ExtendedDataInputStream inputStream) {
		switch (hardwareType) {
		case Assignments.ARP.HARDWARE_TYPE_ETHERNET:
			return inputStream.readMACAddress();
		default:
			throw new IllegalStateException("Unsupported hardware type!");
		}
	}
	
	public static Object readProtocolAddress(short protocolType,
			ExtendedDataInputStream inputStream) {
		switch (protocolType) {
		case Assignments.Ethernet.TYPE_IPV4:
			return inputStream.readIPv4Address();
		default:
			throw new IllegalStateException("Unsupported protocol type!");
		}
	}
	
	@Override
	protected void formatGeneric(ARPPacket packet,
			ExtendedDataOutputStream outputStream) {
		outputStream.writeShort(packet.getHardwareType());
		outputStream.writeShort(packet.getProtocolType());
		outputStream.writeByte(Assignments.ARP.getHardwareLength(packet.getHardwareType()));
		outputStream.writeByte(Assignments.ARP.getProtocolLength(packet.getProtocolType()));
		outputStream.writeShort(packet.getOperation());
		writeHardwareAddress(packet.getSenderHardwareAddress(),
				packet.getHardwareType(), outputStream);
		writeProtocolAddress(packet.getSenderProtocolAddress(),
				packet.getProtocolType(), outputStream);
		writeHardwareAddress(packet.getTargetHardwareAddress(),
				packet.getHardwareType(), outputStream);
		writeProtocolAddress(packet.getTargetProtocolAddress(),
				packet.getProtocolType(), outputStream);
	}
	
	@Override
	public ARPPacket parse(ExtendedDataInputStream inputStream) {
		ARPPacket packet = new ARPPacket();
		
		packet.setHardwareType(inputStream.readShort());
		packet.setProtocolType(inputStream.readShort());
		
		if (inputStream.readByte() != Assignments.ARP.getHardwareLength(packet.getHardwareType())) throw new IllegalStateException(
				"Illegal hardware address length");
		
		if (inputStream.readByte() != Assignments.ARP.getProtocolLength(packet.getProtocolType())) throw new IllegalStateException(
				"Illegal protocol address length");
		
		packet.setOperation(inputStream.readShort());
		packet.setSenderHardwareAddress(readHardwareAddress(
				packet.getHardwareType(), inputStream));
		packet.setSenderProtocolAddress(readProtocolAddress(
				packet.getProtocolType(), inputStream));
		packet.setTargetHardwareAddress(readHardwareAddress(
				packet.getHardwareType(), inputStream));
		packet.setTargetProtocolAddress(readProtocolAddress(
				packet.getProtocolType(), inputStream));
		
		return packet;
	}
	
}