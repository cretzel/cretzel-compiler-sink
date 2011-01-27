package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public class ParameterAstNodeImpl extends IdentifierAstNodeImpl implements
		ParameterAstNode {

	public ParameterAstNodeImpl(String name) {
		super(name);
	}

	@Override
	public Kind getKind() {
		return Kind.PARAMETER;
	}

	@Override
	public int getTag() {
		return PARAMETER;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitParameter(this);
	}

}
