package com.cp.ast.nodes;

import java.util.List;

public interface FunctionInvocationAstNode extends ExpressionAstNode {

	String getName();

	List<ExpressionAstNode> getArguments();

}
