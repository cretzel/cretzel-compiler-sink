package asm;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class AsmTest {

	private static final String CLASS_NAME = "AsmTestMain";

	@Test
	public void test_create_simple_main_class() throws Exception {
		ClassWriter cw = new ClassWriter(0);

		cw.visit(V1_5, ACC_PUBLIC, CLASS_NAME, null, "java/lang/Object", null);

		MethodVisitor mv;
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
					"()V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main",
					"([Ljava/lang/String;)V", null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, CLASS_NAME);
			mv.visitMethodInsn(INVOKESPECIAL, CLASS_NAME, "<init>", "()V");

			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
					"Ljava/io/PrintStream;");
			mv.visitLdcInsn("foo");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print",
					"(Ljava/lang/String;)V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(100, 1);
			mv.visitEnd();
		}

		cw.visitEnd();
		byte[] byteArray = cw.toByteArray();

		Class<?> clazz = new AsmClassloader()
				.defineClass(CLASS_NAME, byteArray);
		Method main = clazz.getMethod("main", String[].class);
		main.invoke(null, (Object) new String[0]);
		System.out.println();
	}
	

	@Test
	public void test_class_printer() throws Exception {
		ClassReader cr = new ClassReader("asm.Foo");
		cr.accept(new AsmClassPrinter(), 0);
	}
}
