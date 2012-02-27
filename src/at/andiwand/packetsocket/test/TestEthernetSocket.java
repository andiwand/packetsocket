package at.andiwand.packetsocket.test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;

import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.packetsocket.EthernetSocket;


public class TestEthernetSocket {
	
	public static void main(String[] args) {
		EthernetSocket socket = null;
		
		try {
			socket = new EthernetSocket(EthernetSocket.PROTOCOL_ALL);
			socket.bind("wlan0");
			socket.enablePromiscMode("wlan0");
			
			MACAddress src = new MACAddress("00:24:8c:fd:fe:96");
			Inet4Address srcIp = (Inet4Address) Inet4Address
					.getByName("192.168.15.112");
			MACAddress dst = MACAddress.BROADCAST;
			Inet4Address dstIp = (Inet4Address) Inet4Address
					.getByName("192.168.15.111");
			
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			DataOutputStream outputStream = new DataOutputStream(
					arrayOutputStream);
			
			outputStream.write(dst.toByteArray());
			outputStream.write(src.toByteArray());
			outputStream.writeShort(0x0806);
			
			outputStream.writeShort(1);
			outputStream.writeShort(0x0800);
			outputStream.write(6);
			outputStream.write(4);
			outputStream.writeShort(1);
			outputStream.write(src.toByteArray());
			outputStream.write(srcIp.getAddress());
			outputStream.write(dst.toByteArray());
			outputStream.write(dstIp.getAddress());
			
			socket.send(arrayOutputStream.toByteArray());
			
			byte[] buffer = new byte[1500];
			while (true) {
				System.out.println(socket.receive(buffer));
				System.out.println(new MACAddress(buffer, 0));
				System.out.println(new MACAddress(buffer, 6));
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Throwable t) {}
		}
	}
	
}