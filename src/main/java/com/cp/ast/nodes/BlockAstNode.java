package com.cp.ast.nodes;

import java.util.List;

public interface BlockAstNode extends AstNode {

	List<AssignmentAstNode> getVariableDeclarations();

	ExpressionAstNode getExpression();
}
