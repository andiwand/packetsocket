package at.andiwand.packetsocket.pdu.formatter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.InternetChecksum;
import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packetsocket.pdu.TCPSegment;
import at.andiwand.packetsocket.pdu.TCPSegment.EndOption;
import at.andiwand.packetsocket.pdu.TCPSegment.MaximumSegmentSizeOption;
import at.andiwand.packetsocket.pdu.TCPSegment.NoOption;
import at.andiwand.packetsocket.pdu.TCPSegment.Option;
import at.andiwand.packetsocket.pdu.TCPSegment.SackPermittedOption;
import at.andiwand.packetsocket.pdu.TCPSegment.TimestampOption;
import at.andiwand.packetsocket.pdu.TCPSegment.WindowScaleOption;


// TODO: ports?
// TODO: implement options
public class TCPSegmentFormatter extends GenericPDUFormatter<TCPSegment> {
	
	private static final Map<Integer, PDUFormatter> PAYLOAD_FORMATTERS = new HashMap<Integer, PDUFormatter>();
	
	static {
		PAYLOAD_FORMATTERS.put(Assignments.TCP.PORT_TELNET,
				new TelnetSegmentFormatter());
		PAYLOAD_FORMATTERS.put(Assignments.TCP.PORT_DNS,
				new DHCPPacketFormatter());
	}
	
	private static void formatOption(Option option, ExtendedDataOutputStream out) {
		if ((option instanceof EndOption) || (option instanceof NoOption)) {
			out.writeByte(option.getOption());
			return;
		}
		
		ExtendedDataOutputStream tmp = new ExtendedDataOutputStream();
		
		if (option instanceof MaximumSegmentSizeOption) {
			tmp.writeShort(((MaximumSegmentSizeOption) option).getMaximumSegmentSize());
		} else if (option instanceof WindowScaleOption) {
			tmp.writeByte(((WindowScaleOption) option).getShiftCnt());
		} else if (option instanceof SackPermittedOption) {
			
		} else if (option instanceof TimestampOption) {
			tmp.writeInt((int) ((TimestampOption) option).getValue());
			tmp.writeInt((int) ((TimestampOption) option).getEchoReply());
		} else {
			throw new IllegalStateException("Unsupported option!");
		}
		
		out.writeByte(option.getOption());
		out.writeByte(2 + tmp.size());
		out.write(tmp.getData());
	}
	
	private static void formatOptions(List<Option> options,
			ExtendedDataOutputStream out) {
		for (Option option : options) {
			formatOption(option, out);
		}
	}
	
	private static byte[] formatOptions(List<Option> options) {
		ExtendedDataOutputStream out = new ExtendedDataOutputStream();
		formatOptions(options, out);
		return out.getData();
	}
	
	private static Option parseOption(ExtendedDataInputStream in) {
		Option result;
		byte option = in.readByte();
		
		switch (option) {
		case Assignments.TCP.OPTION_END:
			result = new EndOption();
			break;
		case Assignments.TCP.OPTION_NO:
			result = new NoOption();
			break;
		case Assignments.TCP.OPTION_MAXIMUM_SEGMENT_SIZE:
			result = new MaximumSegmentSizeOption();
			in.readByte(); // length
			((MaximumSegmentSizeOption) result).setMaximumSegmentSize(in.readShort() & 0xffff);
			break;
		case Assignments.TCP.OPTION_WINDOW_SCALE:
			result = new WindowScaleOption();
			in.readByte(); // length
			((WindowScaleOption) result).setShiftCnt(in.readByte());
			break;
		case Assignments.TCP.OPTION_SACK_PERMITTED:
			result = new SackPermittedOption();
			in.readByte(); // length
			break;
		case Assignments.TCP.OPTION_TIMESTAMP:
			result = new TimestampOption();
			in.readByte(); // length
			((TimestampOption) result).setValue(in.readInt() & 0xffffffffl);
			((TimestampOption) result).setEchoReply(in.readInt() & 0xffffffffl);
			break;
		default:
			throw new IllegalArgumentException("Unsupported option!");
		}
		
		result.setOption(option);
		return result;
	}
	
	private static List<Option> parseOptions(ExtendedDataInputStream in) {
		List<Option> result = new LinkedList<Option>();
		
		while (true) {
			Option option = parseOption(in);
			result.add(option);
			if (in.available() == 0) break;
		}
		
		return result;
	}
	
	@Override
	protected void formatGeneric(TCPSegment segment,
			ExtendedDataOutputStream out) {
		byte[] options = formatOptions(segment.getOptions());
		int dataOffset = 5 + (options.length >> 2);
		if ((options.length % 4) != 0) dataOffset++;
		
		ExtendedDataOutputStream bufferOut = new ExtendedDataOutputStream();
		bufferOut.writeShort(segment.getSourcePort() & 0xffff);
		bufferOut.writeShort(segment.getDestinationPort() & 0xffff);
		bufferOut.writeInt((int) (segment.getSequenceNumber() & 0xffffffffl));
		bufferOut.writeInt((int) (segment.getAcknowledgmentNumber() & 0xffffffffl));
		int tmp = 0;
		tmp |= (dataOffset & 0x0f) << 28;
		tmp |= (segment.getReserved() & 0x3f) << 22;
		tmp |= (segment.getFlags() & 0x3f) << 16;
		tmp |= (segment.getWindow() & 0xffff) << 0;
		bufferOut.writeInt(tmp);
		bufferOut.writeShort(0); // checksum
		bufferOut.writeShort(segment.getUrgentPointer() & 0xffff);
		
		bufferOut.write(options);
		if ((options.length % 4) != 0) {
			for (int i = 0; i < (4 - (options.length % 4)); i++) {
				bufferOut.write(0x01);
			}
		}
		
		if (segment.getPayload() != null) {
			int minPort = Math.min(segment.getSourcePort(),
					segment.getDestinationPort());
			PDUFormatter payloadFormatter = PAYLOAD_FORMATTERS.get(minPort);
			payloadFormatter.format(segment.getPayload(), bufferOut);
		}
		
		int size = bufferOut.size();
		if ((size & 0x01) != 0) bufferOut.writeByte(0);
		
		byte[] header = bufferOut.getData();
		short checksum = InternetChecksum.calculateChecksum(header);
		header[16] = (byte) ((checksum >> 8) & 0xff);
		header[17] = (byte) ((checksum >> 0) & 0xff);
		out.write(header, 0, size);
	}
	
	@Override
	public TCPSegment parse(ExtendedDataInputStream in) {
		TCPSegment segment = new TCPSegment();
		
		segment.setSourcePort(in.readShort() & 0xffff);
		segment.setDestinationPort(in.readShort() & 0xffff);
		segment.setSequenceNumber(in.readInt() & 0xffffffffl);
		segment.setAcknowledgmentNumber(in.readInt() & 0xffffffffl);
		int tmp = in.readInt();
		byte dataOffset = (byte) ((tmp >> 28) & 0x0f);
		segment.setReserved((byte) ((tmp >> 22) & 0x3f));
		segment.setFlags((byte) ((tmp >> 16) & 0x3f));
		segment.setWindow((tmp >> 0) & 0xffff);
		in.readShort(); // checksum
		segment.setUrgentPointer(in.readShort() & 0xffff);
		
		if (dataOffset > 5) {
			byte[] options = new byte[(dataOffset - 5) << 2];
			in.read(options);
			ExtendedDataInputStream optionDataInputStream = new ExtendedDataInputStream(
					options);
			segment.setOptions(parseOptions(optionDataInputStream));
		}
		
		byte[] payloadBuffer = in.readBytes();
		
		if (((segment.getFlags() & Assignments.TCP.FLAG_PSH) != 0)
				&& (payloadBuffer.length > 0)) {
			int minPort = Math.min(segment.getSourcePort(),
					segment.getDestinationPort());
			PDUFormatter payloadFormatter = PAYLOAD_FORMATTERS.get(minPort);
			if (payloadFormatter == null) throw new IllegalArgumentException(
					"Unsupported port!");
			ExtendedDataInputStream payloadInputStream = new ExtendedDataInputStream(
					payloadBuffer);
			PDU payload = payloadFormatter.parse(payloadInputStream);
			segment.setPayload(payload);
		}
		
		return segment;
	}
}
