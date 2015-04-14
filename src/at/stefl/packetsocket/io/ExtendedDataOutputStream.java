package at.stefl.packetsocket.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.mac.MACAddress;
import at.stefl.commons.util.UUIDUtil;


public class ExtendedDataOutputStream extends OutputStream {
	
	private ByteArrayOutputStream outputStream;
	private DataOutputStream dataOutputStream;
	
	public ExtendedDataOutputStream() {
		reset();
	}
	
	public byte[] getData() {
		return outputStream.toByteArray();
	}
	
	@Override
	public void write(int data) {
		try {
			dataOutputStream.write(data);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	@Override
	public void write(byte[] data) {
		try {
			dataOutputStream.write(data);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	@Override
	public void write(byte[] data, int offset, int length) {
		try {
			dataOutputStream.write(data, offset, length);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeByte(int b) {
		try {
			dataOutputStream.writeByte(b);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeBoolean(boolean b) {
		try {
			dataOutputStream.writeBoolean(b);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeShort(int s) {
		try {
			dataOutputStream.writeShort(s);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeInt(int i) {
		try {
			dataOutputStream.writeInt(i);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeLong(long l) {
		try {
			dataOutputStream.writeLong(l);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeFloat(float f) {
		try {
			dataOutputStream.writeFloat(f);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeDouble(double d) {
		try {
			dataOutputStream.writeDouble(d);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeIPv4Address(IPv4Address address) {
		try {
			dataOutputStream.write(address.toByteArray());
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeMACAddress(MACAddress macAddress) {
		try {
			dataOutputStream.write(macAddress.toByteArray());
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void writeUuid(UUID uuid) {
		try {
			dataOutputStream.write(UUIDUtil.uuidToBytes(uuid));
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public int size() {
		return dataOutputStream.size();
	}
	
	public void reset() {
		outputStream = new ByteArrayOutputStream();
		dataOutputStream = new DataOutputStream(outputStream);
	}
	
	@Override
	public void close() {
	}
	
}