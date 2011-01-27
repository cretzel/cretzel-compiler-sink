package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class ProgramAstNodeImpl extends AstNodeImpl implements ProgramAstNode {

	private final MainAstNode main;
	private final FunctionDeclarationsAstNode functionDeclarations;

	public ProgramAstNodeImpl(FunctionDeclarationsAstNode funDeclrs,
			MainAstNode main) {
		this.main = main;
		this.functionDeclarations = funDeclrs;
	}

	public MainAstNode getMain() {
		return main;
	}

	public FunctionDeclarationsAstNode getFunctionDeclarations() {
		return functionDeclarations;
	}

	@Override
	public Kind getKind() {
		return Kind.PROGRAM;
	}

	@Override
	public int getTag() {
		return TOPLEVEL;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitProgram(this);
	}

}
