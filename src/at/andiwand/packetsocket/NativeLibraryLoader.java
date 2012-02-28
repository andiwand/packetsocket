package at.andiwand.packetsocket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.io.StreamUtil;


public class NativeLibraryLoader {
	
	private static final String TMP_PREFIX = "packetsocket";
	
	private static final Map<String, String> LIBRARY_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = -3291315487830681697L;
		
		{
			put("linux", "linux/libpacketsocket.so");
		}
	};
	
	public static void load() throws IOException {
		String os = System.getProperty("os.name").toLowerCase();
		String resource = LIBRARY_MAP.get(os);
		
		if (resource == null)
			throw new IllegalStateException("Unsupported operating system!");
		
		File tmp = File.createTempFile(TMP_PREFIX, "");
		
		InputStream inputStream = ClassLoader
				.getSystemResourceAsStream(resource);
		OutputStream outputStream = new FileOutputStream(tmp);
		StreamUtil.write(inputStream, outputStream);
		
		System.load(tmp.getAbsolutePath());
	}
	
	private NativeLibraryLoader() {}
	
}