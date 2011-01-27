package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class ErroneousAstNodeImpl extends ExpressionAstNodeImpl implements
		ErroneousAstNode {

	@Override
	public Kind getKind() {
		return Kind.ERRONEOUS;
	}

	@Override
	public int getTag() {
		return ERRONEOUS;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitErroneous(this);
	}

}
