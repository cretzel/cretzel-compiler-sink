package asm.asmifier;

import java.io.PrintWriter;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.ASMifierClassVisitor;

public class AsmifierTest implements Opcodes {

	@Test
	public void test_asmify() throws Exception {

		ASMifierClassVisitor asmifier = new ASMifierClassVisitor(
				new PrintWriter(System.out));
		 //ClassReader reader = new ClassReader("asm.asmifier.Counter");
		 ClassReader reader = new ClassReader("asm.asmifier.CounterTarget");
		reader.accept(asmifier, ClassReader.SKIP_DEBUG);
		
	}
}
