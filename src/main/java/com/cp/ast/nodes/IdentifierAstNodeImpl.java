package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class IdentifierAstNodeImpl extends ExpressionAstNodeImpl implements
		IdentifierAstNode {

	private final String name;

	public IdentifierAstNodeImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Kind getKind() {
		return Kind.IDENTIFIER;
	}

	@Override
	public int getTag() {
		return IDENTIFIER;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitIdentifier(this);
	}

}
