package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class NumberLiteralAstNodeImpl extends ExpressionAstNodeImpl implements
		NumberLiteralAstNode {

	private final String value;

	public NumberLiteralAstNodeImpl(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public Kind getKind() {
		return Kind.NUMBER_LITERAL;
	}

	@Override
	public int getTag() {
		return NUMBER_LITERAL;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitNumberLiteral(this);
	}

}
