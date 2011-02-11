package asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class AsmClassPrinter implements ClassVisitor {

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		System.out.printf("%s extends %s", name, superName);
		if (interfaces.length > 0) {
			System.out.printf(" implements %s", interfaces[0]);
		}
		if (interfaces.length > 1) {
			for (int i = 1; i < interfaces.length; i++) {
				System.out.printf(", %s", interfaces[i]);
			}
		}
		System.out.println();
	}

	@Override
	public void visitSource(String source, String debug) {
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return null;
	}

	@Override
	public void visitAttribute(Attribute attr) {
	}

	@Override
	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		return null;
	}

	@Override
	public void visitEnd() {
	}

}
