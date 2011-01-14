package com.cp.ast.nodes;

public interface ProgramAstNode extends AstNode {

	OutputAstNode getOutput();

	DeclarationsAstNode getDeclr();

	boolean hasOutput();

}
