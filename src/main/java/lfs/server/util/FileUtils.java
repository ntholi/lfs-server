/**
 * 
 */
package lfs.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Properties;

import lfs.server.core.Constants;

/**
 * @author Ntholi Nkhatho
 *
 */
public class FileUtils {

	public static String read(String fileName) throws IOException{
		Path path = Paths.get(fileName);
		if(Files.exists(path)) {
			return new String(Files.readAllBytes(Paths.get(fileName)));
		}
		return null;
	}
	
	public static List<String> readLines(String fileName) throws IOException{
		return Files.readAllLines(Paths.get(fileName));
	}
	
	public static Properties readProperties(String configFile) throws IOException {
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(Paths.get(Constants.CONFIG_FILE), 
				StandardOpenOption.CREATE)) {
			props.load(in);
		}
		return props;
	}
	
	public static void write(String fileName, String content) throws IOException{
		Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
	}
	
	public static void append(String fileName, String content) throws IOException{
		Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.APPEND);
	}

	public static void write(String fileName, Properties props) throws IOException {
        try (OutputStream out = Files.newOutputStream(Paths.get(fileName), 
        		StandardOpenOption.CREATE)) {
        	props.store(out, "This file should never be edited or deleted".toUpperCase());
        }
	}
}
