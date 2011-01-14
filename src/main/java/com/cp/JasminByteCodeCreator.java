package com.cp;

import jasmin.ClassFile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;

import com.cp.ast.AstAnnotations;
import com.cp.ast.AstAnnotations.AnnotationType;
import com.cp.ast.nodes.AssignmentAstNode;
import com.cp.ast.nodes.AstNodeImpl;
import com.cp.ast.nodes.BinaryAstNode;
import com.cp.ast.nodes.DeclarationAstNode;
import com.cp.ast.nodes.DeclarationsAstNode;
import com.cp.ast.nodes.ErroneousAstNode;
import com.cp.ast.nodes.ExpressionAstNode;
import com.cp.ast.nodes.IdentifierAstNode;
import com.cp.ast.nodes.NumberLiteralAstNode;
import com.cp.ast.nodes.OutputAstNode;
import com.cp.ast.nodes.ParenthesizedAstNode;
import com.cp.ast.nodes.ProgramAstNode;
import com.cp.ast.visitor.SimpleVisitor;

public class JasminByteCodeCreator implements SimpleVisitor {

	private static final String CLASS_NAME = "Main";

	private final AstAnnotations annotations;
	private StringBuilder src = new StringBuilder();
	private int indent = 0;

	public JasminByteCodeCreator(AstAnnotations annotations) {
		this.annotations = annotations;
	}

	public void indent() {
		indent += 4;
	}

	public void outdent() {
		if (indent == 0) {
			throw new IllegalStateException(
					"Trying to outdent on actual indentation=0");
		}
		indent -= 4;

	}

	private void appendLine(String line) {
		for (int i = 0; i < indent; i++) {
			src.append(" ");
		}
		src.append(line).append("\n");
	}

	public String getSrc() {
		return src.toString();
	}

	@Override
	public void visitProgram(ProgramAstNode program) {
		appendLine(".class public " + CLASS_NAME);
		appendLine(".super java/lang/Object");
		appendLine("");
		appendLine("");
		appendLine(".method public <init>()V");
		indent();
		appendLine("aload_0");
		appendLine("invokenonvirtual java/lang/Object/<init>()V");
		appendLine("return");
		outdent();
		appendLine(".end method");
		appendLine("");
		appendLine(".method public static main([Ljava/lang/String;)V");
		indent();
		appendLine(".limit locals "
				+ annotations.get(program, AnnotationType.NUMBER_OF_VARIABLES)
				+ 1);
		appendLine(".limit stack 100");

		program.getDeclr().accept(this);
		OutputAstNode output = program.getOutput();
		if (output != null) {
			output.accept(this);
		}

		appendLine("return");
		outdent();
		appendLine(".end method");
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

		// IdentifierAstNode id = assignment.getId();
		int varNumber = (Integer) annotations.get(assignment,
				AnnotationType.VARIABLE_NUMBER);
		appendLine("istore " + varNumber);
	}

	@Override
	public void visitBinary(BinaryAstNode binary) {
		binary.getLhs().accept(this);
		binary.getRhs().accept(this);

		int opcode = binary.getOpcode();
		switch (opcode) {
		case AstNodeImpl.PLUS:
			appendLine("iadd");
			break;
		case AstNodeImpl.MINUS:
			appendLine("isub");
			break;
		case AstNodeImpl.MULT:
			appendLine("imul");
			break;
		case AstNodeImpl.DIV:
			appendLine("idiv");
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

		appendLine("iload " + varNumber);
	}

	@Override
	public void visitNumberLiteral(NumberLiteralAstNode numberLiteral) {
		String value = numberLiteral.getValue();
		appendLine("ldc " + value);
	}

	@Override
	public void visitParenthesized(ParenthesizedAstNode parenthesized) {
		parenthesized.getExpr().accept(this);
	}

	@Override
	public void visitOutput(OutputAstNode output) {

		appendLine("getstatic java/lang/System/out Ljava/io/PrintStream;");

		output.getExpr().accept(this);

		appendLine("invokevirtual java/io/PrintStream/print(I)V");

	}

	@Override
	public void visitErroneous(ErroneousAstNode erroneous) {
		// TODO Auto-generated method stub

	}

	public void createByteCode(OutputStream outStream) throws IOException,
			Exception {
		ClassFile classFile = new ClassFile();
		StringReader srcReader = new StringReader(getSrc());
		classFile.readJasmin(srcReader, CLASS_NAME, true);
		classFile.write(outStream);
	}

}