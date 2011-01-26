package com.cp;

import static org.objectweb.asm.Opcodes.*;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class AsmTest {

	@Test
	public void testname() throws Exception {
		ClassWriter cw = new ClassWriter(0);

		cw.visit(V1_5, ACC_PUBLIC, "Main", null, "java/lang/Object", null);

		// TODO <init>???

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main",
				"([Ljava/lang/String;)V", null, null);

		{
			mv.visitTypeInsn(NEW, "Main");
			//mv.visitMethodInsn(INVOKESPECIAL, "Main", "<init>", "()V");
		}

		{
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
					"Ljava/io/PrintStream;");
			mv.visitLdcInsn("foo");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print",
					"(Ljava/lang/String;)V");
		}

		mv.visitInsn(RETURN);
		mv.visitMaxs(100, 1);

		cw.visitEnd();
		byte[] byteArray = cw.toByteArray();

		FileOutputStream fout = new FileOutputStream(new File("Main.class"));
		fout.write(byteArray);
		fout.close();
	}
}
