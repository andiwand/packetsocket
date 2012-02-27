package at.andiwand.packetsocket.pdu.formatter;

import at.andiwand.packetsocket.io.ExtendedDataInputStream;
import at.andiwand.packetsocket.io.ExtendedDataOutputStream;
import at.andiwand.packetsocket.pdu.PDU;


public abstract class GenericPDUFormatter<T extends PDU> extends PDUFormatter {
	
	@Override
	@SuppressWarnings("unchecked")
	public void format(PDU pdu, ExtendedDataOutputStream outputStream) {
		formatGeneric((T) pdu, outputStream);
	}
	
	protected abstract void formatGeneric(T pdu,
			ExtendedDataOutputStream outputStream);
	
	public T parse(byte[] buffer) {
		return parse(new ExtendedDataInputStream(buffer));
	}
	
	public T parse(byte[] buffer, int offset, int length) {
		return parse(new ExtendedDataInputStream(buffer, offset, length));
	}
	
	public abstract T parse(ExtendedDataInputStream inputStream);
	
}