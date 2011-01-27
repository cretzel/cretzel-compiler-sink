package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class ParenthesizedAstNodeImpl extends ExpressionAstNodeImpl implements
		ParenthesizedAstNode {

	private ExpressionAstNode expr;

	public ParenthesizedAstNodeImpl(ExpressionAstNode exprAstNode) {
		this.expr = exprAstNode;
	}

	public ExpressionAstNode getExpr() {
		return expr;
	}

	@Override
	public Kind getKind() {
		return Kind.PARENTHESIZED;
	}

	@Override
	public int getTag() {
		return PARENTHESIZED;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitParenthesized(this);
	}

}
