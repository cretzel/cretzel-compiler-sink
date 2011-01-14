package com.cp.ast.nodes;


public interface BinaryAstNode extends TermAstNode {

	int getOpcode();

	AstNode getLhs();

	AstNode getRhs();

}
