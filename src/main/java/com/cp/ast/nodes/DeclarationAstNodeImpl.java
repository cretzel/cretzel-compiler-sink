package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public abstract class DeclarationAstNodeImpl extends AstNodeImpl implements
		DeclarationAstNode {

	@Override
	public Kind getKind() {
		return Kind.DECLARATION;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitDeclaration(this);
	}

	@Override
	public int getTag() {
		return DECLARATION;
	}

}
