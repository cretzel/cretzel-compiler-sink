package com.cp.ast.nodes;

import java.util.List;

public interface FunctionDeclarationAstNode extends DeclarationAstNode {

	IdentifierAstNode getId();

	List<ParameterAstNode> getParameters();
	
	BlockAstNode getBlock();

}
