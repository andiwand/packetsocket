package at.stefl.packetsocket.pdu.formatter;

import at.stefl.packetsocket.io.ExtendedDataInputStream;
import at.stefl.packetsocket.io.ExtendedDataOutputStream;
import at.stefl.packetsocket.pdu.PDU;


public abstract class GenericPDUFormatter<T extends PDU> extends PDUFormatter {
	
	@Override
	@SuppressWarnings("unchecked")
	public void format(PDU pdu, ExtendedDataOutputStream out) {
		formatGeneric((T) pdu, out);
	}
	
	protected abstract void formatGeneric(T pdu, ExtendedDataOutputStream out);
	
	public T parse(byte[] buffer) {
		return parse(new ExtendedDataInputStream(buffer));
	}
	
	public T parse(byte[] buffer, int offset, int length) {
		return parse(new ExtendedDataInputStream(buffer, offset, length));
	}
	
	public abstract T parse(ExtendedDataInputStream in);
	
}