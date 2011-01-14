package com.cp.ast.nodes;

import com.cp.ast.TreeInfo;
import com.cp.ast.visitor.SimpleVisitor;

public class BinaryAstNodeImpl extends AstNodeImpl implements BinaryAstNode {

	private final int opcode;
	private final AstNode lhs;
	private final AstNode rhs;

	public BinaryAstNodeImpl(int opcode, AstNode lhs, AstNode rhs) {
		this.opcode = opcode;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public Kind getKind() {
		return TreeInfo.tagToKind(getTag());
	}

	public int getTag() {
		return opcode;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitBinary(this);
	}

	public int getOpcode() {
		return opcode;
	}

	public AstNode getLhs() {
		return lhs;
	}

	public AstNode getRhs() {
		return rhs;
	}

}
