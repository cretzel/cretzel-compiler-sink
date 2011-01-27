package com.cp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.cp.ast.AstAnnotations;
import com.cp.ast.AstAnnotations.AnnotationType;
import com.cp.ast.nodes.AssignmentAstNode;
import com.cp.ast.nodes.AstNodeImpl;
import com.cp.ast.nodes.BinaryAstNode;
import com.cp.ast.nodes.BlockAstNode;
import com.cp.ast.nodes.DeclarationAstNode;
import com.cp.ast.nodes.DeclarationsAstNode;
import com.cp.ast.nodes.ErroneousAstNode;
import com.cp.ast.nodes.ExpressionAstNode;
import com.cp.ast.nodes.FunctionDeclarationAstNode;
import com.cp.ast.nodes.FunctionDeclarationsAstNode;
import com.cp.ast.nodes.FunctionInvocationAstNode;
import com.cp.ast.nodes.IdentifierAstNode;
import com.cp.ast.nodes.IfElseAstNode;
import com.cp.ast.nodes.MainAstNode;
import com.cp.ast.nodes.NumberLiteralAstNode;
import com.cp.ast.nodes.OutputAstNode;
import com.cp.ast.nodes.ParameterAstNode;
import com.cp.ast.nodes.ParenthesizedAstNode;
import com.cp.ast.nodes.ProgramAstNode;
import com.cp.ast.visitor.SimpleVisitor;

public class AsmByteCodeCreator implements SimpleVisitor, Opcodes {

	private static final String CLASS_NAME = "Main";

	private final AstAnnotations annotations;

	private ClassWriter cw;
	private MethodVisitor mv;

	public AsmByteCodeCreator(AstAnnotations annotations) {
		this.annotations = annotations;
	}

	@Override
	public void visitProgram(ProgramAstNode program) {
		cw = new ClassWriter(0);

		cw.visit(V1_5, ACC_PUBLIC, CLASS_NAME, null, "java/lang/Object", null);

		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv = null;

		FunctionDeclarationsAstNode functionDeclarations = program
				.getFunctionDeclarations();
		functionDeclarations.accept(this);

		program.getMain().accept(this);

		cw.visitEnd();
	}

	@Override
	public void visitFunctionDeclarations(
			FunctionDeclarationsAstNode functionDeclarations) {
		List<FunctionDeclarationAstNode> declarations = functionDeclarations
				.getDeclarations();
		for (FunctionDeclarationAstNode function : declarations) {
			function.accept(this);
		}
	}

	@Override
	public void visitMain(MainAstNode main) {

		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main",
				"([Ljava/lang/String;)V", null, null);

		main.getDeclr().accept(this);
		OutputAstNode output = main.getOutput();
		if (output != null) {
			output.accept(this);
		}

		mv.visitInsn(RETURN);
		int maxStack = 100;
		int maxLocals = (Integer) annotations.get(main,
				AnnotationType.NUMBER_OF_VARIABLES) + 2;
		mv.visitMaxs(maxStack, maxLocals);

		mv = null;

	}

	@Override
	public void visitDeclarations(DeclarationsAstNode declrs) {
		List<DeclarationAstNode> declarations = declrs.getDeclarations();
		for (DeclarationAstNode declr : declarations) {
			declr.accept(this);
		}
	}

	@Override
	public void visitDeclaration(DeclarationAstNode declr) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visitAssignment(AssignmentAstNode assignment) {
		ExpressionAstNode expr = assignment.getExpr();
		expr.accept(this);

		int varNumber = (Integer) annotations.get(assignment,
				AnnotationType.VARIABLE_NUMBER);
		mv.visitVarInsn(ISTORE, varNumber);
	}

	@Override
	public void visitBinary(BinaryAstNode binary) {
		binary.getLhs().accept(this);
		binary.getRhs().accept(this);

		int opcode = binary.getOpcode();
		switch (opcode) {
		case AstNodeImpl.PLUS:
			mv.visitInsn(IADD);
			break;
		case AstNodeImpl.MINUS:
			mv.visitInsn(ISUB);
			break;
		case AstNodeImpl.MULT:
			mv.visitInsn(IMUL);
			break;
		case AstNodeImpl.DIV:
			mv.visitInsn(IDIV);
			break;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public void visitIdentifier(IdentifierAstNode identifier) {
		// Probably fragile: An identifier can appear in an assignment and in a
		// usage position. The Parser could create different AstNodes for each
		// case, e.g. a class IdentifierAccessAstNode as a subclass of
		// IdentifierAstNode, but for the moment let's try to never visit
		// identifier ast nodes in assignment position, i.e. only in usage.

		if (!annotations.has(identifier, AnnotationType.VARIABLE_NUMBER)) {
			throw new IllegalStateException("Identifier "
					+ identifier.getName() + " is not defined");
		}
		int varNumber = (Integer) annotations.get(identifier,
				AnnotationType.VARIABLE_NUMBER);

		mv.visitVarInsn(ILOAD, varNumber);
	}

	@Override
	public void visitNumberLiteral(NumberLiteralAstNode numberLiteral) {
		String value = numberLiteral.getValue();
		mv.visitIntInsn(BIPUSH, Integer.valueOf(value));
	}

	@Override
	public void visitParenthesized(ParenthesizedAstNode parenthesized) {
		parenthesized.getExpr().accept(this);
	}

	@Override
	public void visitFunctionDeclaration(FunctionDeclarationAstNode function) {

		String paramsStr = "";
		for (int i = 0; i < function.getParameters().size(); i++) {
			paramsStr += "I";
		}

		final String name = function.getId().getName();
		final String desc = "(" + paramsStr + ")I";
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, name, desc, null, null);

		function.getBlock().accept(this);
		mv.visitInsn(IRETURN);

		mv.visitMaxs(100, (Integer) annotations.get(function,
				AnnotationType.NUMBER_OF_VARIABLES));
		mv = null;
	}

	@Override
	public void visitParameter(ParameterAstNode parameterAstNodeImpl) {

	}

	@Override
	public void visitBlock(BlockAstNode block) {
		List<AssignmentAstNode> variableDeclarations = block
				.getVariableDeclarations();
		for (AssignmentAstNode declr : variableDeclarations) {
			declr.accept(this);
		}

		block.getExpression().accept(this);
	}

	@Override
	public void visitFunctionInvocation(
			FunctionInvocationAstNode functionInvocation) {

		String paramsStr = "";
		List<ExpressionAstNode> arguments = functionInvocation.getArguments();
		for (ExpressionAstNode arg : arguments) {
			arg.accept(this);
			paramsStr += "I";
		}

		final String name = functionInvocation.getName();
		final String desc = "(" + paramsStr + ")I";
		mv.visitMethodInsn(INVOKESTATIC, CLASS_NAME, name, desc);

	}

	@Override
	public void visitOutput(OutputAstNode output) {

		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
				"Ljava/io/PrintStream;");

		output.getExpr().accept(this);

		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print",
				"(I)V");

	}

	@Override
	public void visitIfElse(IfElseAstNode ifElse) {

		ifElse.getCondition().accept(this);

		Label elseLabel = new Label();
		Label afterLabel = new Label();

		mv.visitJumpInsn(IFEQ, elseLabel);
		ifElse.getThenBlock().accept(this);
		mv.visitJumpInsn(GOTO, afterLabel);

		mv.visitLabel(elseLabel);
		ifElse.getElseBlock().accept(this);

		mv.visitLabel(afterLabel);

	}

	@Override
	public void visitErroneous(ErroneousAstNode erroneous) {
	}

	public void writeByteCode(OutputStream outStream) throws IOException,
			Exception {
		outStream.write(toByteArray());
	}

	private byte[] toByteArray() {
		return cw.toByteArray();
	}

}
