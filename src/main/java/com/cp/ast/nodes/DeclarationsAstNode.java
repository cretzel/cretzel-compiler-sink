package com.cp.ast.nodes;

import java.util.List;

public interface DeclarationsAstNode extends AstNode {

	void addDeclaration(DeclarationAstNode declr);

	List<DeclarationAstNode> getDeclarations();

}
