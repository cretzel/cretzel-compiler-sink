package com.cp.ast.nodes;

import java.util.List;

import com.cp.ast.visitor.SimpleVisitor;

public class FunctionInvocationAstNodeImpl extends ExpressionAstNodeImpl
		implements FunctionInvocationAstNode {

	private final String name;
	private final List<ExpressionAstNode> arguments;

	public FunctionInvocationAstNodeImpl(String name,
			List<ExpressionAstNode> arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	public List<ExpressionAstNode> getArguments() {
		return arguments;
	}

	public String getName() {
		return name;
	}

	@Override
	public Kind getKind() {
		return Kind.FUNCTION_INVOCATION;
	}

	@Override
	public int getTag() {
		return FUNCTIONINVOCATION;
	}

	@Override
	public void accept(SimpleVisitor visitor) {
		visitor.visitFunctionInvocation(this);
	}

}
