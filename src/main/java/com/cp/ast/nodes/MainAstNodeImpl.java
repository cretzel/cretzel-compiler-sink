package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class MainAstNodeImpl extends AstNodeImpl implements MainAstNode {

	private final OutputAstNode output;
	private final DeclarationsAstNode declr;

	public MainAstNodeImpl(DeclarationsAstNode declarations,
			OutputAstNode output) {
		this.declr = declarations;
		this.output = output;
	}

	public OutputAstNode getOutput() {
		return output;
	}

	@Override
	public boolean hasOutput() {
		return output != null;
	}

	public DeclarationsAstNode getDeclr() {
		return declr;
	}

	@Override
	public Kind getKind() {
		return Kind.MAIN;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitMain(this);
	}

	@Override
	public int getTag() {
		return MAIN;
	}

}
