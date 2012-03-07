package at.andiwand.packetsocket.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import at.andiwand.library.io.StreamUtil;
import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.util.UUIDUtil;


public class ExtendedDataInputStream extends InputStream {
	
	private final byte[] data;
	private final int offset;
	private final int length;
	
	private ByteArrayInputStream inputStream;
	private DataInputStream dataInputStream;
	
	public ExtendedDataInputStream(byte[] data) {
		this.data = data;
		offset = 0;
		length = data.length;
		
		reset();
	}
	
	public ExtendedDataInputStream(byte[] data, int offset, int length) {
		this.data = data;
		this.offset = offset;
		this.length = length;
		
		reset();
	}
	
	public byte[] getData() {
		return data;
	}
	
	@Override
	public int read() {
		try {
			return dataInputStream.read();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	@Override
	public int read(byte[] data) {
		try {
			return dataInputStream.read(data);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	@Override
	public int read(byte[] data, int offset, int length) {
		try {
			return dataInputStream.read(data, offset, length);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public byte readByte() {
		try {
			return dataInputStream.readByte();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public int readUnsignedByte() {
		try {
			return dataInputStream.readUnsignedByte();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public boolean readBoolean() {
		try {
			return dataInputStream.readBoolean();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public short readShort() {
		try {
			return dataInputStream.readShort();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public int readUnsignedShort() {
		try {
			return dataInputStream.readUnsignedShort();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public int readInt() {
		try {
			return dataInputStream.readInt();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public long readUnsignedInt() {
		try {
			return dataInputStream.readInt() & 0xffffffffl;
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public long readLong() {
		try {
			return dataInputStream.readLong();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public float readFloat() {
		try {
			return dataInputStream.readFloat();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public double readDouble() {
		try {
			return dataInputStream.readDouble();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public IPv4Address readIPv4Address() {
		try {
			byte[] tmp = new byte[4];
			dataInputStream.read(tmp);
			return new IPv4Address(tmp);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public MACAddress readMACAddress() {
		try {
			byte[] tmp = new byte[6];
			dataInputStream.read(tmp);
			return new MACAddress(tmp);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public UUID readUuid() {
		try {
			byte[] tmp = new byte[16];
			dataInputStream.read(tmp);
			return UUIDUtil.bytesToUuid(tmp);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public byte[] readBytes() {
		try {
			return StreamUtil.readBytes(dataInputStream);
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	public void reset() {
		inputStream = new ByteArrayInputStream(data, offset, length);
		dataInputStream = new DataInputStream(inputStream);
	}
	
}