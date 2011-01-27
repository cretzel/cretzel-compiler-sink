package com.cp.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import com.cp.ast.visitor.SimpleVisitor;

public class DeclarationsAstNodeImpl extends AstNodeImpl implements
		DeclarationsAstNode {

	private List<DeclarationAstNode> declarations = new ArrayList<DeclarationAstNode>();

	public DeclarationsAstNodeImpl() {
		super();
	}

	public void addDeclaration(DeclarationAstNode declr) {
		this.declarations.add(declr);
	}

	public List<DeclarationAstNode> getDeclarations() {
		return declarations;
	}

	@Override
	public Kind getKind() {
		return Kind.DECLARATIONS;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitDeclarations(this);
	}

	@Override
	public int getTag() {
		return DECLARATIONS;
	}

}
