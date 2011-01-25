package com.cp.ast.nodes;

public interface IfElseAstNode extends ExpressionAstNode {

	ExpressionAstNode getCondition();

	BlockAstNode getThenBlock();

	BlockAstNode getElseBlock();

}
