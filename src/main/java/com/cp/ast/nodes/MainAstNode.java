package com.cp.ast.nodes;

public interface MainAstNode extends AstNode {

	boolean hasOutput();

	OutputAstNode getOutput();

	DeclarationsAstNode getDeclr();

}
