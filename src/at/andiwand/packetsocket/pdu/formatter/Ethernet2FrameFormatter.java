package at.andiwand.packetsocket.pdu.formatter;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.Assignments;
import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.Ethernet2Frame;


public class Ethernet2FrameFormatter extends
		GenericPDUFormatter<Ethernet2Frame> {
	
	private static final Map<Short, PDUFormatter> PAYLOAD_FORMATTERS = new HashMap<Short, PDUFormatter>();
	
	static {
		PAYLOAD_FORMATTERS.put(Assignments.Ethernet.TYPE_IPV4,
				new IPv4PacketFormatter());
		PAYLOAD_FORMATTERS.put(Assignments.Ethernet.TYPE_ARP,
				new ARPPacketFormatter());
	}
	
	private static PDUFormatter getPayloadFormatter(short type) {
		PDUFormatter result = PAYLOAD_FORMATTERS.get(type);
		if (result == null) throw new IllegalArgumentException(
				"Unregistered type! " + type);
		return result;
	}
	
	@Override
	protected void formatGeneric(Ethernet2Frame frame,
			ExtendedDataOutputStream outputStream) {
		outputStream.writeMACAddress(frame.getDestination());
		outputStream.writeMACAddress(frame.getSource());
		outputStream.writeShort(frame.getType());
		
		PDUFormatter payloadFormatter = getPayloadFormatter(frame.getType());
		payloadFormatter.format(frame.getPayload(), outputStream);
	}
	
	@Override
	public Ethernet2Frame parse(ExtendedDataInputStream inputStream) {
		Ethernet2Frame frame = new Ethernet2Frame();
		
		frame.setDestination(inputStream.readMACAddress());
		frame.setSource(inputStream.readMACAddress());
		frame.setType(inputStream.readShort());
		
		PDUFormatter payloadFormatter = getPayloadFormatter(frame.getType());
		frame.setPayload(payloadFormatter.parse(inputStream));
		
		return frame;
	}
	
}