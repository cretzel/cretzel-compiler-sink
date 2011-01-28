package asm.asmifier;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CounterAdapter extends ClassAdapter {

	public static final String FQN = "asm.asmifier.CounterX";
	private static final String SUPER_NAME = "asm/asmifier/Counter";
	private static final String NAME = SUPER_NAME + "X";

	public CounterAdapter(ClassVisitor cv) {
		super(cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		cv.visit(version, access, NAME, signature, SUPER_NAME, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
				exceptions);
		if (name.equals("increment")) {
			return new CounterIncrementMethodVisitor(mv);
		}
		if (name.equals("count")) {
			return new CounterCountMethodVisitor(mv);
		}
		return mv;
	}

	private class CounterIncrementMethodVisitor extends MethodAdapter implements
			Opcodes {

		private CounterIncrementMethodVisitor(MethodVisitor mv) {
			super(mv);
		}

		@Override
		public void visitCode() {
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
					"Ljava/io/PrintStream;");
			mv.visitLdcInsn("increment()");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
					"(Ljava/lang/String;)V");
		}

		@Override
		public void visitFieldInsn(int opcode, String owner, String name,
				String desc) {
			if (owner.equals(SUPER_NAME)) {
				mv.visitFieldInsn(opcode, NAME, name, desc);
			} else {
				super.visitFieldInsn(opcode, owner, name, desc);
			}
		}

	}

	private class CounterCountMethodVisitor extends MethodAdapter implements
			Opcodes {

		private CounterCountMethodVisitor(MethodVisitor mv) {
			super(mv);
		}

		@Override
		public void visitCode() {
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
					"Ljava/io/PrintStream;");
			mv.visitLdcInsn("count()");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
					"(Ljava/lang/String;)V");
		}

		@Override
		public void visitFieldInsn(int opcode, String owner, String name,
				String desc) {
			if (owner.equals(SUPER_NAME)) {
				mv.visitFieldInsn(opcode, NAME, name, desc);
			} else {
				super.visitFieldInsn(opcode, owner, name, desc);
			}
		}

	}

}
