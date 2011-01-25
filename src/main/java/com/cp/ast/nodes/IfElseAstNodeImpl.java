package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class IfElseAstNodeImpl extends AstNodeImpl implements IfElseAstNode {

	private final ExpressionAstNode condition;
	private final BlockAstNode thenBlock;
	private final BlockAstNode elseBlock;

	public IfElseAstNodeImpl(ExpressionAstNode condition, BlockAstNode thenBlock,
			BlockAstNode elseBlock) {
		this.condition = condition;
		this.thenBlock = thenBlock;
		this.elseBlock = elseBlock;
	}

	public ExpressionAstNode getCondition() {
		return condition;
	}

	public BlockAstNode getThenBlock() {
		return thenBlock;
	}

	public BlockAstNode getElseBlock() {
		return elseBlock;
	}

	@Override
	public Kind getKind() {
		return Kind.IF_ELSE;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitIfElse(this);
	}

	@Override
	public int getTag() {
		return IFELSE;
	}

}
