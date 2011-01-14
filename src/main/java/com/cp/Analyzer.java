package com.cp;

import com.cp.ast.AstAnnotations;
import com.cp.ast.nodes.AssignmentAstNode;
import com.cp.ast.nodes.BinaryAstNode;
import com.cp.ast.nodes.DeclarationAstNode;
import com.cp.ast.nodes.DeclarationsAstNode;
import com.cp.ast.nodes.ErroneousAstNode;
import com.cp.ast.nodes.IdentifierAstNode;
import com.cp.ast.nodes.NumberLiteralAstNode;
import com.cp.ast.nodes.OutputAstNode;
import com.cp.ast.nodes.ParenthesizedAstNode;
import com.cp.ast.nodes.ProgramAstNode;
import com.cp.ast.visitor.SimpleVisitor;

public class Analyzer implements SimpleVisitor {

	private final AstAnnotations annotations;
	private final SymbolTable symbolTable;

	public Analyzer() {
		annotations = new AstAnnotations();
		symbolTable = new SymbolTable(annotations);
	}

	public AstAnnotations getAnnotations() {
		return annotations;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	@Override
	public void visitProgram(ProgramAstNode program) {
		program.getDeclr().accept(this);

		symbolTable.leftDeclarations(program);

		if (program.hasOutput()) {
			program.getOutput().accept(this);
		}
	}

	@Override
	public void visitDeclarations(DeclarationsAstNode declrs) {
		for (DeclarationAstNode decl : declrs.getDeclarations()) {
			decl.accept(this);
		}
	}

	@Override
	public void visitDeclaration(DeclarationAstNode declr) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visitAssignment(AssignmentAstNode assignment) {
		assignment.getExpr().accept(this);
		this.symbolTable.enter(assignment.getId().getName(), assignment);
	}

	@Override
	public void visitIdentifier(IdentifierAstNode identifier) {
		// Probably fragile: An identifier can appear in an assignment and in a
		// usage position. The Parser could create different AstNodes for each
		// case, e.g. a class IdentifierAccessAstNode as a subclass of
		// IdentifierAstNode, but for the moment let's try to never visit
		// identifier ast nodes in assignment position, i.e. only in usage.

		symbolTable.variableAccess(identifier.getName(), identifier);
	}

	@Override
	public void visitBinary(BinaryAstNode binary) {
		binary.getLhs().accept(this);
		binary.getRhs().accept(this);
	}

	@Override
	public void visitErroneous(ErroneousAstNode erroneous) {
		System.err.println("Visiting erroneous");
	}

	@Override
	public void visitNumberLiteral(NumberLiteralAstNode numberLiteral) {

	}

	@Override
	public void visitParenthesized(ParenthesizedAstNode parenthesized) {
		parenthesized.getExpr().accept(this);
	}

	@Override
	public void visitOutput(OutputAstNode outputAstNodeImpl) {
		outputAstNodeImpl.getExpr().accept(this);
	}

}
