package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public final class Utils {

	private Utils() {
	}

	public static File createTmpFolder() {
		try {
			File f = File.createTempFile("compilertmp", "");
			f.delete();
			f.mkdir();
			return f;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String executeMainMethod(File classFile) {
		return executeMainMethod(classFile.getParentFile(), classFile.getName()
				.substring(0, classFile.getName().length() - 6));
	}

	public static String executeMainMethod(File classFolder, String className) {

		System.err.println("Executing cp: " + classFolder.getAbsolutePath()
				+ ", class: " + className);

		ClassLoader l;
		try {
			l = new URLClassLoader(new URL[] { classFolder.toURI().toURL() });
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		}

		try {
			Class<?> clazz = l.loadClass(className);
			Method m = clazz.getMethod("main", (new String[0]).getClass());
			m.setAccessible(true);
			String out = executeMainMethod(m);
			return out;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String executeMainMethod(Method m) {

		// StdOut sichern und umbiegen
		PrintStream orgOut = System.out;
		PrintStream orgErr = System.err;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bos, true);
		ByteArrayOutputStream bosErr = new ByteArrayOutputStream();
		PrintStream err = new PrintStream(bosErr, true);
		System.setOut(out);
		System.setErr(err);

		// Methode ausführen
		try {
			m.invoke(null, (Object) new String[0]);
		} catch (InvocationTargetException e) {
			e.getCause().printStackTrace(out);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// StdOut wiederherstellen
		System.setOut(orgOut);
		System.setErr(orgErr);

		String sOut = new String(bos.toByteArray());
		// String sErr = new String(bosErr.toByteArray());

		return sOut;
	}

}
