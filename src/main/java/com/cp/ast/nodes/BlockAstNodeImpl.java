package com.cp.ast.nodes;

import java.util.List;

import com.cp.ast.visitor.SimpleVisitor;

public class BlockAstNodeImpl extends AstNodeImpl implements BlockAstNode {

	private final List<AssignmentAstNode> declarations;
	private final ExpressionAstNode expr;

	public BlockAstNodeImpl(List<AssignmentAstNode> declarations,
			ExpressionAstNode expr) {
		this.declarations = declarations;
		this.expr = expr;
	}

	@Override
	public Kind getKind() {
		return Kind.BLOCK;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitBlock(this);
	}

	@Override
	public List<AssignmentAstNode> getVariableDeclarations() {
		return declarations;
	}

	@Override
	public ExpressionAstNode getExpression() {
		return expr;
	}

	@Override
	public int getTag() {
		return BLOCK;
	}

}
