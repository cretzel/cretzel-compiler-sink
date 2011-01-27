package com.cp.ast.nodes;

public interface AssignmentAstNode extends DeclarationAstNode {

	IdentifierAstNode getId();

	ExpressionAstNode getExpr();

}
