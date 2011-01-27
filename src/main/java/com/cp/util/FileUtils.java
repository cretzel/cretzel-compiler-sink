package com.cp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class FileUtils {

	private FileUtils() {
	}

	public static String readFile(String filename) {
		return readFile(new File(filename));
	}

	public static String readFile(File file) {
		StringBuilder contents = new StringBuilder();
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			try {
				String line = null;

				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return contents.toString();
	}

}
