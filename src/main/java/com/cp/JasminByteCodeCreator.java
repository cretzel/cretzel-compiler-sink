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

public class JasminByteCodeCreator implements SimpleVisitor {

	private static final String CLASS_NAME = "Main";

	private final AstAnnotations annotations;
	private StringBuilder src = new StringBuilder();
	private int indent = 0;

	private int labelCounter;

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

	private void appendLine(String line, Object... args) {
		appendLine(String.format(line, args));
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

		FunctionDeclarationsAstNode functionDeclarations = program
				.getFunctionDeclarations();
		functionDeclarations.accept(this);

		program.getMain().accept(this);
	}

	@Override
	public void visitFunctionDeclarations(
			FunctionDeclarationsAstNode functionDeclarations) {
		labelCounter = 0;
		List<FunctionDeclarationAstNode> declarations = functionDeclarations
				.getDeclarations();
		for (FunctionDeclarationAstNode function : declarations) {
			function.accept(this);
		}
	}

	@Override
	public void visitMain(MainAstNode main) {

		appendLine(".method public static main([Ljava/lang/String;)V");
		indent();
		appendLine(".limit stack 100");
		appendLine(".limit locals "
				+ ((Integer) annotations.get(main,
						AnnotationType.NUMBER_OF_VARIABLES) + 2));

		main.getDeclr().accept(this);
		OutputAstNode output = main.getOutput();
		if (output != null) {
			output.accept(this);
		}

		appendLine("return");
		outdent();
		appendLine(".end method");
		appendLine("");

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
		appendLine("bipush " + value);
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
		appendLine(".method public static %s(%s)%s",
				function.getId().getName(), paramsStr, "I");
		indent();
		appendLine(".limit stack %d", 100);
		appendLine(".limit locals %d", ((Integer) annotations.get(function,
				AnnotationType.NUMBER_OF_VARIABLES)));

		function.getBlock().accept(this);
		appendLine("ireturn");

		// block
		outdent();
		appendLine(".end method");
		appendLine("");

	}

	@Override
	public void visitParameter(ParameterAstNode parameterAstNodeImpl) {
		// TODO Auto-generated method stub

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

		appendLine("invokestatic Main/%s(%s)I", functionInvocation.getName(),
				paramsStr);

	}

	@Override
	public void visitOutput(OutputAstNode output) {

		appendLine("getstatic java/lang/System/out Ljava/io/PrintStream;");

		output.getExpr().accept(this);

		appendLine("invokevirtual java/io/PrintStream/print(I)V");

	}

	@Override
	public void visitIfElse(IfElseAstNode ifElse) {

		ifElse.getCondition().accept(this);

		int elseLabel = labelCounter++;
		int afterLabel = labelCounter++;

		appendLine("ifeq label%d", elseLabel);
		ifElse.getThenBlock().accept(this);
		appendLine("goto label%d", afterLabel);

		appendLine("label%d:", elseLabel);
		ifElse.getElseBlock().accept(this);

		appendLine("label%d:", afterLabel);

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
