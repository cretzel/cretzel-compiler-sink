package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class AssignmentAstNodeImpl extends AstNodeImpl implements
		AssignmentAstNode {

	private final IdentifierAstNode id;
	private final ExpressionAstNode expr;

	public AssignmentAstNodeImpl(IdentifierAstNode id, ExpressionAstNode expr) {
		super();
		this.id = id;
		this.expr = expr;
	}

	@Override
	public Kind getKind() {
		return Kind.ASSIGNMENT;
	}

	public int getTag() {
		return ASSIGNMENT;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitAssignment(this);
	}

	public IdentifierAstNode getId() {
		return id;
	}

	public ExpressionAstNode getExpr() {
		return expr;
	}

}
