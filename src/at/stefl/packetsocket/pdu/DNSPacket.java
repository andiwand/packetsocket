package at.stefl.packetsocket.pdu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DNSPacket extends PDU {
	
	public static class DNSQuestion {
		private String domainName;
		private short type;
		private short clazz;
		
		public String getDomainName() {
			return domainName;
		}
		
		public short getType() {
			return type;
		}
		
		public short getClazz() {
			return clazz;
		}
		
		public void setDomainName(String domainName) {
			this.domainName = domainName;
		}
		
		public void setType(short type) {
			this.type = type;
		}
		
		public void setClazz(short clazz) {
			this.clazz = clazz;
		}
	}
	
	public static class DNSRessourceRecord {
		private String domainName;
		private short type;
		private short clazz;
		private long timeToLive;
		private byte[] ressource;
		
		public String getDomainName() {
			return domainName;
		}
		
		public short getType() {
			return type;
		}
		
		public short getClazz() {
			return clazz;
		}
		
		public long getTimeToLive() {
			return timeToLive;
		}
		
		public byte[] getRessource() {
			return Arrays.copyOf(ressource, ressource.length);
		}
		
		public void setDomainName(String domainName) {
			this.domainName = domainName;
		}
		
		public void setType(short type) {
			this.type = type;
		}
		
		public void setClazz(short clazz) {
			this.clazz = clazz;
		}
		
		public void setTimeToLive(long timeToLive) {
			this.timeToLive = timeToLive;
		}
		
		public void setRessource(byte[] ressource) {
			this.ressource = Arrays.copyOf(ressource, ressource.length);
		}
	}
	
	private short id;
	private boolean response;
	private byte operationCode;
	private boolean authoritativeAnswer;
	private boolean trunCation;
	private boolean recursionDesired;
	private boolean recursionAvailable;
	private byte reserved;
	private byte responseCode;
	private List<DNSQuestion> questions = new ArrayList<DNSQuestion>();
	private List<DNSRessourceRecord> answers = new ArrayList<DNSRessourceRecord>();
	private List<DNSRessourceRecord> nameServers = new ArrayList<DNSRessourceRecord>();
	private List<DNSRessourceRecord> additionalRecords = new ArrayList<DNSRessourceRecord>();
	
	public short getId() {
		return id;
	}
	
	public boolean isResponse() {
		return response;
	}
	
	public byte getOperationCode() {
		return operationCode;
	}
	
	public boolean isAuthoritativeAnswer() {
		return authoritativeAnswer;
	}
	
	public boolean isTrunCation() {
		return trunCation;
	}
	
	public boolean isRecursionDesired() {
		return recursionDesired;
	}
	
	public boolean isRecursionAvailable() {
		return recursionAvailable;
	}
	
	public byte getReserved() {
		return reserved;
	}
	
	public byte getResponseCode() {
		return responseCode;
	}
	
	public int getQuestionCount() {
		return questions.size();
	}
	
	public int getAnswerCount() {
		return answers.size();
	}
	
	public int getNameServerCount() {
		return nameServers.size();
	}
	
	public int getAdditionalRecordCount() {
		return additionalRecords.size();
	}
	
	public List<DNSQuestion> getQuestions() {
		return new ArrayList<DNSQuestion>(questions);
	}
	
	public List<DNSRessourceRecord> getAnswers() {
		return new ArrayList<DNSRessourceRecord>(answers);
	}
	
	public List<DNSRessourceRecord> getNameServers() {
		return new ArrayList<DNSRessourceRecord>(nameServers);
	}
	
	public List<DNSRessourceRecord> getAdditionalRecords() {
		return new ArrayList<DNSRessourceRecord>(additionalRecords);
	}
	
	public void setId(short id) {
		this.id = id;
	}
	
	public void setResponse(boolean response) {
		this.response = response;
	}
	
	public void setOperationCode(byte operationCode) {
		this.operationCode = operationCode;
	}
	
	public void setAuthoritativeAnswer(boolean authoritativeAnswer) {
		this.authoritativeAnswer = authoritativeAnswer;
	}
	
	public void setTrunCation(boolean trunCation) {
		this.trunCation = trunCation;
	}
	
	public void setRecursionDesired(boolean recursionDesired) {
		this.recursionDesired = recursionDesired;
	}
	
	public void setRecursionAvailable(boolean recursionAvailable) {
		this.recursionAvailable = recursionAvailable;
	}
	
	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}
	
	public void setResponseCode(byte responseCode) {
		this.responseCode = responseCode;
	}
	
	public void setQuestions(List<DNSQuestion> questions) {
		this.questions = new ArrayList<DNSQuestion>(questions);
	}
	
	public void setAnswers(List<DNSRessourceRecord> answers) {
		this.answers = new ArrayList<DNSRessourceRecord>(answers);
	}
	
	public void setNameServers(List<DNSRessourceRecord> nameServers) {
		this.nameServers = new ArrayList<DNSRessourceRecord>(nameServers);
	}
	
	public void setAdditionalRecords(List<DNSRessourceRecord> additionalRecords) {
		this.additionalRecords = new ArrayList<DNSRessourceRecord>(
				additionalRecords);
	}
	
	public void addQuestion(DNSQuestion question) {
		questions.add(question);
	}
	
	public void addAnswer(DNSRessourceRecord answer) {
		answers.add(answer);
	}
	
	public void addNameServer(DNSRessourceRecord nameServer) {
		nameServers.add(nameServer);
	}
	
	public void addAdditionalRecord(DNSRessourceRecord additionalRecord) {
		additionalRecords.add(additionalRecord);
	}
	
	public void removeQuestion(DNSQuestion question) {
		questions.remove(question);
	}
	
	public void removeAnswer(DNSRessourceRecord answer) {
		answers.remove(answer);
	}
	
	public void removeNameServer(DNSRessourceRecord nameServer) {
		nameServers.remove(nameServer);
	}
	
	public void removeAdditionalRecord(DNSRessourceRecord additionalRecords) {
		questions.remove(additionalRecords);
	}
	
}