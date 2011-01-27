package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class OutputAstNodeImpl extends AstNodeImpl implements OutputAstNode {

	private ExpressionAstNode expr;

	public OutputAstNodeImpl(ExpressionAstNode expr) {
		super();
		this.expr = expr;
	}

	public ExpressionAstNode getExpr() {
		return expr;
	}

	public void setExpr(ExpressionAstNode expr) {
		this.expr = expr;
	}

	@Override
	public Kind getKind() {
		return Kind.OUTPUT;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitOutput(this);
	}

	@Override
	public int getTag() {
		return OUTPUT;
	}

}
