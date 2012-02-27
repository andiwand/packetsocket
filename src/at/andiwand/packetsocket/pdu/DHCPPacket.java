package at.andiwand.packetsocket.pdu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;


public class DHCPPacket extends PDU {
	
	public static abstract class Option extends PDU {
		private byte type;
		
		public final byte getType() {
			return type;
		}
		
		public final void setType(byte type) {
			this.type = type;
		}
	}
	
	public static abstract class IPOptionTemplate extends Option {
		private IPv4Address address;
		
		public IPv4Address getAddress() {
			return address;
		}
		
		public void setAddress(IPv4Address address) {
			this.address = address;
		}
	}
	
	public static abstract class IPListOptionTemplate extends Option {
		private List<IPv4Address> addresses = new LinkedList<IPv4Address>();
		
		public List<IPv4Address> getAddresses() {
			return Collections.unmodifiableList(addresses);
		}
		
		public int size() {
			return addresses.size();
		}
		
		public boolean isEmpty() {
			return addresses.isEmpty();
		}
		
		public void setAddresses(List<IPv4Address> addresses) {
			this.addresses = new LinkedList<IPv4Address>(addresses);
		}
		
		public void addAddress(IPv4Address address) {
			addresses.add(address);
		}
		
		public void removeAddress(IPv4Address address) {
			addresses.remove(address);
		}
	}
	
	public static class SubnetMaskOption extends Option {
		private SubnetMask subnetMask;
		
		public SubnetMask getSubnetMask() {
			return subnetMask;
		}
		
		public void setSubnetMask(SubnetMask subnetMask) {
			this.subnetMask = subnetMask;
		}
	}
	
	public static class RouterOption extends IPListOptionTemplate {}
	
	public static class NameServerOption extends IPListOptionTemplate {}
	
	public static class RequestedIPAddressOption extends IPOptionTemplate {}
	
	public static class MessageTypeOption extends Option {
		private byte type;
		
		public byte getMessageType() {
			return type;
		}
		
		public void setMessageType(byte type) {
			this.type = type;
		}
	}
	
	public static class ServerIdentifierOption extends IPOptionTemplate {}
	
	private byte operation;
	private short hardwareType;
	private short hops;
	private int transactionId;
	private int secondsElapsed;
	private short flags;
	private IPv4Address clientAddess;
	private IPv4Address yourAddess;
	private IPv4Address serverAddess;
	private IPv4Address relayAgentAddess;
	private Object clientHardwareAddress;
	private String serverName;
	private String file;
	private List<Option> options = new LinkedList<Option>();
	
	public byte getOperation() {
		return operation;
	}
	
	public short getHardwareType() {
		return hardwareType;
	}
	
	public short getHops() {
		return hops;
	}
	
	public int getTransactionId() {
		return transactionId;
	}
	
	public int getSecondsElapsed() {
		return secondsElapsed;
	}
	
	public short getFlags() {
		return flags;
	}
	
	public IPv4Address getClientAddess() {
		return clientAddess;
	}
	
	public IPv4Address getYourAddess() {
		return yourAddess;
	}
	
	public IPv4Address getServerAddess() {
		return serverAddess;
	}
	
	public IPv4Address getRelayAgentAddess() {
		return relayAgentAddess;
	}
	
	public Object getClientHardwareAddress() {
		return clientHardwareAddress;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public String getFile() {
		return file;
	}
	
	public List<Option> getOptions() {
		return Collections.unmodifiableList(options);
	}
	
	public Option getOption(byte type) {
		for (Option option : options) {
			if (option.getType() == type) return option;
		}
		
		return null;
	}
	
	public void setOperation(byte operation) {
		this.operation = operation;
	}
	
	public void setHardwareType(short hardwareType) {
		this.hardwareType = hardwareType;
	}
	
	public void setHops(short hops) {
		this.hops = hops;
	}
	
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	
	public void setSecondsElapsed(int secondsElapsed) {
		this.secondsElapsed = secondsElapsed;
	}
	
	public void setFlags(short flags) {
		this.flags = flags;
	}
	
	public void setClientAddress(IPv4Address clientAddess) {
		this.clientAddess = clientAddess;
	}
	
	public void setYourAddress(IPv4Address yourAddess) {
		this.yourAddess = yourAddess;
	}
	
	public void setServerAddress(IPv4Address serverAddess) {
		this.serverAddess = serverAddess;
	}
	
	public void setRelayAgentAddress(IPv4Address relayAgentAddess) {
		this.relayAgentAddess = relayAgentAddess;
	}
	
	public void setClientHardwareAddress(Object clientHardwareAddress) {
		this.clientHardwareAddress = clientHardwareAddress;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public void setOptions(List<Option> options) {
		this.options = new LinkedList<Option>(options);
	}
	
	public void addOption(Option option) {
		options.add(option);
	}
	
	public void removeOption(Option option) {
		options.remove(option);
	}
	
}