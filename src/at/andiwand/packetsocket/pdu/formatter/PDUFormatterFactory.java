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
	
	private static final Map<Class<? extends PDU>, Class<? extends PDUFormatter>> FORMATTER_CLASS_MAP = new HashMap<Class<? extends PDU>, Class<? extends PDUFormatter>>() {
		private static final long serialVersionUID = -4035097606420938382L;
		
		{
			put(Ethernet2Frame.class, Ethernet2FrameFormatter.class);
			put(ARPPacket.class, ARPPacketFormatter.class);
			put(IPv4Packet.class, IPv4PacketFormatter.class);
			put(ICMPPacket.class, ICMPPacketFormatter.class);
			put(TCPSegment.class, TCPSegmentFormatter.class);
			put(UDPSegment.class, UDPSegmentFormatter.class);
			put(DHCPPacket.class, DHCPPacketFormatter.class);
			put(TelnetSegment.class, TelnetSegmentFormatter.class);
		}
	};
	
	public static final PDUFormatterFactory FACTORY = new PDUFormatterFactory();
	
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
			throw new IllegalStateException("Cannot create instance!");
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