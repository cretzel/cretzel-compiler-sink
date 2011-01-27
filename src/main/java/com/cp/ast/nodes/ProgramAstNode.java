package com.cp.ast.nodes;

public interface ProgramAstNode extends AstNode {

	MainAstNode getMain();

	FunctionDeclarationsAstNode getFunctionDeclarations();

}
