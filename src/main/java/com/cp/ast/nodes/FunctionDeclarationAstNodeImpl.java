package com.cp.ast.nodes;

import java.util.List;

import com.cp.ast.visitor.SimpleVisitor;

public class FunctionDeclarationAstNodeImpl extends AstNodeImpl implements
		FunctionDeclarationAstNode {

	private final IdentifierAstNode idAstNode;
	private final List<ParameterAstNode> parameters;
	private final BlockAstNode blockAstNode;

	public FunctionDeclarationAstNodeImpl(IdentifierAstNode idAstNode,
			List<ParameterAstNode> parameters, BlockAstNode blockAstNode) {
		this.idAstNode = idAstNode;
		this.parameters = parameters;
		this.blockAstNode = blockAstNode;
	}

	@Override
	public IdentifierAstNode getId() {
		return idAstNode;
	}

	@Override
	public List<ParameterAstNode> getParameters() {
		return parameters;
	}

	@Override
	public BlockAstNode getBlock() {
		return blockAstNode;
	}

	@Override
	public Kind getKind() {
		return Kind.FUNCTIONDECLARATION;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitFunctionDeclaration(this);
	}

	@Override
	public int getTag() {
		return FUNCTION;
	}

}
