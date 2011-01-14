package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class ProgramAstNodeImpl extends AstNodeImpl implements ProgramAstNode {

	private final OutputAstNode output;
	private final DeclarationsAstNode declr;

	public ProgramAstNodeImpl(DeclarationsAstNode declrAstNode,
			OutputAstNode outputAstNode) {
		this.output = outputAstNode;
		this.declr = declrAstNode;
	}

	@Override
	public Kind getKind() {
		return Kind.PROGRAM;
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
	public int getTag() {
		return TOPLEVEL;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitProgram(this);
	}

}
