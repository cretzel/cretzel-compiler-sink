package com.cp.ast.nodes;

import java.util.List;

public interface FunctionDeclarationsAstNode extends AstNode {

	void addDeclaration(FunctionDeclarationAstNode declr);

	List<FunctionDeclarationAstNode> getDeclarations();

}
