package at.stefl.packetsocket.test;

import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.mac.MACAddress;
import at.stefl.packetsocket.EthernetSocket;
import at.stefl.packetsocket.pdu.Ethernet2Frame;
import at.stefl.packetsocket.pdu.ICMPPacket;
import at.stefl.packetsocket.pdu.IPv4Packet;
import at.stefl.packetsocket.pdu.formatter.Ethernet2FrameFormatter;


public class LocalICMPTest {
	
	public static void main(String[] args) throws Throwable {
		EthernetSocket socket = new EthernetSocket(EthernetSocket.PROTOCOL_ALL);
		socket.bind("lo");
		socket.enablePromiscMode("lo");
		
		IPv4Address source = new IPv4Address("192.168.15.112");
		IPv4Address destination = new IPv4Address("192.168.15.112");
		
		ICMPPacket.EchoPayload echo = new ICMPPacket.EchoPayload();
		echo.setIdentifier(1);
		echo.setSequenceNumber(1);
		echo.setData(new byte[56]);
		
		ICMPPacket icmp = new ICMPPacket();
		icmp.setType(8);
		icmp.setCode(0);
		icmp.setPayload(echo);
		
		IPv4Packet ip = new IPv4Packet();
		ip.setVersion(4);
		ip.setIdentication(1);
		ip.setTimeToLive(64);
		ip.setSource(source);
		ip.setDestination(destination);
		ip.setProtocol(1);
		ip.setPayload(icmp);
		
		Ethernet2Frame frame = new Ethernet2Frame();
		frame.setSource(MACAddress.EMPTY);
		frame.setDestination(MACAddress.EMPTY);
		frame.setType(2048);
		frame.setPayload(ip);
		
		Ethernet2FrameFormatter frameFormatter = new Ethernet2FrameFormatter();
		byte[] frameBuffer = frameFormatter.format(frame);
		
		socket.send(frameBuffer);
	}
	
}