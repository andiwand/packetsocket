package at.stefl.packetsocket.pdu.formatter;

import java.util.Arrays;

import at.stefl.commons.network.Assignments;
import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.ip.SubnetMask;
import at.stefl.packetsocket.io.ExtendedDataInputStream;
import at.stefl.packetsocket.io.ExtendedDataOutputStream;
import at.stefl.packetsocket.pdu.DHCPPacket;
import at.stefl.packetsocket.pdu.DHCPPacket.IPListOptionTemplate;
import at.stefl.packetsocket.pdu.DHCPPacket.IPOptionTemplate;
import at.stefl.packetsocket.pdu.DHCPPacket.MessageTypeOption;
import at.stefl.packetsocket.pdu.DHCPPacket.NameServerOption;
import at.stefl.packetsocket.pdu.DHCPPacket.Option;
import at.stefl.packetsocket.pdu.DHCPPacket.RequestedIPAddressOption;
import at.stefl.packetsocket.pdu.DHCPPacket.RouterOption;
import at.stefl.packetsocket.pdu.DHCPPacket.ServerIdentifierOption;
import at.stefl.packetsocket.pdu.DHCPPacket.SubnetMaskOption;


// TODO change address length system
// TODO change address read/write system
public class DHCPPacketFormatter extends GenericPDUFormatter<DHCPPacket> {
	
	public static void writeHardwareAddress(Object address, short hardwareType,
			ExtendedDataOutputStream outputStream) {
		ExtendedDataOutputStream addressOutputStream = new ExtendedDataOutputStream();
		ARPPacketFormatter.writeHardwareAddress(address, hardwareType,
				addressOutputStream);
		
		outputStream.write(addressOutputStream.getData(), 0,
				Assignments.DHCP.SIZE_CLIENT_HARDWARE_ADDRESS);
	}
	
	public static Object readHardwareAddress(short hardwareType,
			ExtendedDataInputStream inputStream) {
		byte[] addressBuffer = new byte[Assignments.DHCP.SIZE_CLIENT_HARDWARE_ADDRESS];
		inputStream.read(addressBuffer);
		
		ExtendedDataInputStream addressInputStream = new ExtendedDataInputStream(
				addressBuffer);
		return ARPPacketFormatter.readHardwareAddress(hardwareType,
				addressInputStream);
	}
	
	protected void formatGeneric(DHCPPacket packet,
			ExtendedDataOutputStream outputStream) {
		outputStream.writeByte(packet.getOperation());
		outputStream.writeByte(packet.getHardwareType());
		outputStream.writeByte(Assignments.ARP.getHardwareLength(packet.getHardwareType()));
		outputStream.writeByte(packet.getHops());
		outputStream.writeInt(packet.getTransactionId());
		outputStream.writeShort(packet.getSecondsElapsed());
		outputStream.writeShort(packet.getFlags());
		outputStream.writeIPv4Address(packet.getClientAddess());
		outputStream.writeIPv4Address(packet.getYourAddess());
		outputStream.writeIPv4Address(packet.getServerAddess());
		outputStream.writeIPv4Address(packet.getRelayAgentAddess());
		writeHardwareAddress(packet.getClientHardwareAddress(),
				packet.getHardwareType(), outputStream);
		outputStream.write(Arrays.copyOf(packet.getServerName().getBytes(
				Assignments.DHCP.CHARSET), Assignments.DHCP.SIZE_SERVER_NAME));
		outputStream.write(Arrays.copyOf(packet.getServerName().getBytes(
				Assignments.DHCP.CHARSET), Assignments.DHCP.SIZE_FILE));
		outputStream.writeInt(Assignments.DHCP.MAGIC_COOKIE_DHCP);
		
		for (Option option : packet.getOptions()) {
			outputStream.writeByte(option.getType());
			
			switch (option.getType()) {
			case Assignments.DHCP.OPTION_SUBNET_MASK:
				SubnetMaskOption subnetMaskOption = (SubnetMaskOption) option;
				outputStream.writeByte(Assignments.ARP.IPV4_ADDRESS_LENGTH);
				outputStream.writeIPv4Address(subnetMaskOption.getSubnetMask().toIPv4Address());
				break;
			case Assignments.DHCP.OPTION_REQUESTED_IP_ADDRESS:
			case Assignments.DHCP.OPTION_SERVER_IDENTIFIER:
				IPOptionTemplate ipOption = (IPOptionTemplate) option;
				outputStream.writeByte(Assignments.ARP.IPV4_ADDRESS_LENGTH);
				outputStream.writeIPv4Address(ipOption.getAddress());
				break;
			case Assignments.DHCP.OPTION_ROUTER:
			case Assignments.DHCP.OPTION_NAME_SERVER:
				IPListOptionTemplate ipList = (IPListOptionTemplate) option;
				if (ipList.isEmpty()) throw new IllegalArgumentException(
						"Empty list!");
				
				outputStream.writeByte(Assignments.ARP.IPV4_ADDRESS_LENGTH
						* ipList.size());
				for (IPv4Address address : ipList.getAddresses())
					outputStream.writeIPv4Address(address);
				
				break;
			case Assignments.DHCP.OPTION_MESSAGE_TYPE:
				MessageTypeOption messageType = (MessageTypeOption) option;
				outputStream.writeByte(1);
				outputStream.writeByte(messageType.getType());
				break;
			default:
				throw new IllegalStateException("Unreachable section!");
			}
		}
		
		outputStream.writeByte(Assignments.DHCP.OPTION_END);
	}
	
	public DHCPPacket parse(ExtendedDataInputStream inputStream) {
		DHCPPacket packet = new DHCPPacket();
		
		byte[] stringBuffer = new byte[Assignments.DHCP.SIZE_FILE];
		
		packet.setOperation(inputStream.readByte());
		packet.setHardwareType(inputStream.readByte());
		if (inputStream.readByte() != Assignments.ARP.getHardwareLength(packet.getHardwareType())) throw new IllegalStateException(
				"Illegal hardware address length!");
		packet.setHops((short) inputStream.readUnsignedByte());
		packet.setTransactionId(inputStream.readInt());
		packet.setSecondsElapsed(inputStream.readUnsignedShort());
		packet.setFlags(inputStream.readShort());
		packet.setClientAddress(inputStream.readIPv4Address());
		packet.setYourAddress(inputStream.readIPv4Address());
		packet.setServerAddress(inputStream.readIPv4Address());
		packet.setRelayAgentAddress(inputStream.readIPv4Address());
		packet.setClientHardwareAddress(readHardwareAddress(
				packet.getHardwareType(), inputStream));
		inputStream.read(stringBuffer, 0, Assignments.DHCP.SIZE_SERVER_NAME);
		packet.setServerName(new String(stringBuffer, 0,
				Assignments.DHCP.SIZE_SERVER_NAME, Assignments.DHCP.CHARSET).trim());
		inputStream.read(stringBuffer);
		packet.setFile(new String(stringBuffer, Assignments.DHCP.CHARSET).trim());
		inputStream.readInt(); // magic cookie
		
		while (true) {
			byte optionType = inputStream.readByte();
			
			if (optionType == Assignments.DHCP.OPTION_PAD) continue;
			if (optionType == Assignments.DHCP.OPTION_END) break;
			
			int optionLength = inputStream.readUnsignedByte();
			
			byte[] optionBuffer = new byte[optionLength];
			inputStream.read(optionBuffer);
			ExtendedDataInputStream optionInputStream = new ExtendedDataInputStream(
					optionBuffer);
			
			switch (optionType) {
			case Assignments.DHCP.OPTION_SUBNET_MASK:
				SubnetMaskOption subnetMaskOption = new SubnetMaskOption();
				subnetMaskOption.setSubnetMask(new SubnetMask(
						inputStream.readIPv4Address()));
				packet.addOption(subnetMaskOption);
				break;
			case Assignments.DHCP.OPTION_REQUESTED_IP_ADDRESS:
				IPOptionTemplate ipOptionTemplate;
				ipOptionTemplate = new RequestedIPAddressOption();
			case Assignments.DHCP.OPTION_SERVER_IDENTIFIER:
				ipOptionTemplate = new ServerIdentifierOption();
				ipOptionTemplate.setAddress(inputStream.readIPv4Address());
				packet.addOption(ipOptionTemplate);
				break;
			case Assignments.DHCP.OPTION_ROUTER:
				IPListOptionTemplate ipList;
				ipList = new RouterOption();
			case Assignments.DHCP.OPTION_NAME_SERVER:
				ipList = new NameServerOption();
				
				for (int i = 0; i < optionLength; i += Assignments.ARP.IPV4_ADDRESS_LENGTH)
					ipList.addAddress(inputStream.readIPv4Address());
				
				packet.addOption(ipList);
				break;
			case Assignments.DHCP.OPTION_MESSAGE_TYPE:
				MessageTypeOption messageType = new MessageTypeOption();
				messageType.setType(optionInputStream.readByte());
				packet.addOption(messageType);
				break;
			default:
				optionInputStream.close();
				throw new IllegalStateException("Unknown option!");
			}
			
			optionInputStream.close();
		}
		
		return packet;
	}
	
}