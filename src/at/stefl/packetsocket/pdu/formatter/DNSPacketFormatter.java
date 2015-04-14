package at.stefl.packetsocket.pdu.formatter;

import java.util.ArrayList;
import java.util.List;

import at.stefl.packetsocket.io.ExtendedDataInputStream;
import at.stefl.packetsocket.io.ExtendedDataOutputStream;
import at.stefl.packetsocket.pdu.DNSPacket;
import at.stefl.packetsocket.pdu.DNSPacket.DNSQuestion;
import at.stefl.packetsocket.pdu.DNSPacket.DNSRessourceRecord;


// TODO: constants (separator, charset)
public class DNSPacketFormatter extends GenericPDUFormatter<DNSPacket> {
	
	private static void formatDomainName(String domainName,
			ExtendedDataOutputStream out) {
		String[] parts = domainName.split("\\.");
		
		for (String part : parts) {
			out.write(part.length());
			out.write(part.getBytes());
		}
		
		out.write(0);
	}
	
	private static String parseDomainName(ExtendedDataInputStream in,
			byte[] packetBuffer) {
		String result = "";
		
		int firstByte = in.read();
		
		if ((firstByte & 0xc0) == 0xc0) {
			int offset = ((firstByte & 0x3f) << 8) | ((in.read() & 0xff) << 0);
			in = new ExtendedDataInputStream(packetBuffer, offset,
					packetBuffer.length - offset);
			return parseDomainName(in, packetBuffer);
		}
		
		for (int partSize = firstByte; partSize > 0; partSize = in.read()) {
			byte[] buffer = new byte[partSize];
			in.read(buffer);
			result += new String(buffer);
			result += ".";
		}
		
		return result;
	}
	
	@Override
	protected void formatGeneric(DNSPacket packet, ExtendedDataOutputStream out) {
		out.writeShort(packet.getId());
		short tmp = 0;
		tmp |= (packet.getResponseCode() & 0x0f) << 0;
		tmp |= (packet.getReserved() & 0x07) << 4;
		tmp |= (packet.isRecursionAvailable() ? 1 : 0) << 7;
		tmp |= (packet.isRecursionDesired() ? 1 : 0) << 8;
		tmp |= (packet.isTrunCation() ? 1 : 0) << 9;
		tmp |= (packet.isAuthoritativeAnswer() ? 1 : 0) << 10;
		tmp |= (packet.getOperationCode() & 0x0f) << 11;
		tmp |= (packet.isResponse() ? 1 : 0) << 15;
		out.writeShort(tmp);
		out.writeShort(packet.getQuestionCount() & 0xffff);
		out.writeShort(packet.getAnswerCount() & 0xffff);
		out.writeShort(packet.getNameServerCount() & 0xffff);
		out.writeShort(packet.getAdditionalRecordCount() & 0xffff);
		
		for (DNSQuestion question : packet.getQuestions()) {
			formatDomainName(question.getDomainName(), out);
			out.writeShort(question.getType());
			out.writeShort(question.getClazz());
		}
		
		List<List<DNSRessourceRecord>> recordsLists = new ArrayList<List<DNSRessourceRecord>>();
		recordsLists.add(packet.getAnswers());
		recordsLists.add(packet.getNameServers());
		recordsLists.add(packet.getAdditionalRecords());
		
		for (List<DNSRessourceRecord> records : recordsLists) {
			for (DNSRessourceRecord record : records) {
				formatDomainName(record.getDomainName(), out);
				out.writeShort(record.getType());
				out.writeShort(record.getClazz());
				out.writeInt((int) (record.getTimeToLive() & 0xffffffffl));
				out.writeShort(record.getRessource().length & 0xffff);
				out.write(record.getRessource());
			}
		}
	}
	
	@Override
	public DNSPacket parse(ExtendedDataInputStream in) {
		DNSPacket packet = new DNSPacket();
		
		byte[] packetBuffer = in.readBytes();
		in = new ExtendedDataInputStream(packetBuffer);
		
		packet.setId(in.readShort());
		short tmp = in.readShort();
		packet.setResponseCode((byte) ((tmp >> 0) & 0x0f));
		packet.setReserved((byte) ((tmp >> 4) & 0x07));
		packet.setRecursionAvailable(((tmp >> 7) & 0x01) == 1);
		packet.setRecursionDesired(((tmp >> 8) & 0x01) == 1);
		packet.setTrunCation(((tmp >> 9) & 0x01) == 1);
		packet.setAuthoritativeAnswer(((tmp >> 10) & 0x01) == 1);
		packet.setOperationCode((byte) ((tmp >> 11) & 0x0f));
		packet.setResponse(((tmp >> 15) & 0x01) == 1);
		int questionCount = in.readUnsignedShort();
		int answerCount = in.readUnsignedShort();
		int nameServerCount = in.readUnsignedShort();
		int additionalRecordCount = in.readUnsignedShort();
		
		for (int i = 0; i < questionCount; i++) {
			DNSQuestion question = new DNSQuestion();
			question.setDomainName(parseDomainName(in, packetBuffer));
			question.setType(in.readShort());
			question.setClazz(in.readShort());
			packet.addQuestion(question);
		}
		
		int nameServerEnd = answerCount + nameServerCount;
		int recordCount = nameServerEnd + additionalRecordCount;
		
		for (int i = 0; i < recordCount; i++) {
			DNSRessourceRecord record = new DNSRessourceRecord();
			
			record.setDomainName(parseDomainName(in, packetBuffer));
			record.setType(in.readShort());
			record.setClazz(in.readShort());
			record.setTimeToLive(in.readInt() & 0xffffffffl);
			int ressourceSize = in.readUnsignedShort();
			byte[] ressource = new byte[ressourceSize];
			in.read(ressource);
			record.setRessource(ressource);
			
			if (i < answerCount) packet.addAnswer(record);
			else if (i < nameServerEnd) packet.addNameServer(record);
			else packet.addAdditionalRecord(record);
		}
		
		return packet;
	}
}