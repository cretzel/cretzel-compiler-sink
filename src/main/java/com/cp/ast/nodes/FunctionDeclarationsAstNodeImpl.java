package com.cp.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import com.cp.ast.visitor.SimpleVisitor;

public class FunctionDeclarationsAstNodeImpl extends AstNodeImpl implements
		FunctionDeclarationsAstNode {

	private List<FunctionDeclarationAstNode> declarations = new ArrayList<FunctionDeclarationAstNode>();

	public FunctionDeclarationsAstNodeImpl() {
		super();
	}

	public void addDeclaration(FunctionDeclarationAstNode declr) {
		this.declarations.add(declr);
	}

	public List<FunctionDeclarationAstNode> getDeclarations() {
		return declarations;
	}

	@Override
	public Kind getKind() {
		return Kind.FUNCTION_DECLARATIONS;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitFunctionDeclarations(this);
	}

	@Override
	public int getTag() {
		return FUNCTIONDECLARATIONS;
	}

}
