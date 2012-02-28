package at.andiwand.packetsocket.pdu.formatter;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.packetsocket.pdu.ARPPacket;
import at.andiwand.packetsocket.pdu.DHCPPacket;
import at.andiwand.packetsocket.pdu.Ethernet2Frame;
import at.andiwand.packetsocket.pdu.ICMPPacket;
import at.andiwand.packetsocket.pdu.IPv4Packet;
import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packetsocket.pdu.TCPSegment;
import at.andiwand.packetsocket.pdu.TelnetSegment;
import at.andiwand.packetsocket.pdu.UDPSegment;


public class PDUFormatterFactory {
	
	private static final Map<Class<? extends PDU>, Class<? extends PDUFormatter>> FORMATTER_CLASS_MAP = new HashMap<Class<? extends PDU>, Class<? extends PDUFormatter>>();
	
	public static final PDUFormatterFactory FACTORY = new PDUFormatterFactory();
	
	static {
		FORMATTER_CLASS_MAP.put(Ethernet2Frame.class,
				Ethernet2FrameFormatter.class);
		FORMATTER_CLASS_MAP.put(ARPPacket.class, ARPPacketFormatter.class);
		FORMATTER_CLASS_MAP.put(IPv4Packet.class, IPv4PacketFormatter.class);
		FORMATTER_CLASS_MAP.put(ICMPPacket.class, ICMPPacketFormatter.class);
		FORMATTER_CLASS_MAP.put(TCPSegment.class, TCPSegmentFormatter.class);
		FORMATTER_CLASS_MAP.put(UDPSegment.class, UDPSegmentFormatter.class);
		FORMATTER_CLASS_MAP.put(DHCPPacket.class, DHCPPacketFormatter.class);
		FORMATTER_CLASS_MAP.put(TelnetSegment.class,
				TelnetSegmentFormatter.class);
	}
	
	private final Map<Class<? extends PDU>, Class<? extends PDUFormatter>> formatterClassMap = new HashMap<Class<? extends PDU>, Class<? extends PDUFormatter>>(
			FORMATTER_CLASS_MAP);
	
	public Class<? extends PDUFormatter> getFormatterClass(
			Class<? extends PDU> pduClass) {
		return formatterClassMap.get(pduClass);
	}
	
	public PDUFormatter getFormatterInstance(Class<? extends PDU> pduClass) {
		try {
			return getFormatterClass(pduClass).newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	public void putFormatterClass(Class<? extends PDU> pduClass,
			Class<? extends PDUFormatter> formatterClass) {
		formatterClassMap.put(pduClass, formatterClass);
	}
	
	public void removeFormatterClass(Class<? extends PDU> pduClass) {
		formatterClassMap.remove(pduClass);
	}
	
}