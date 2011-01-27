package com.cp.exception;

import com.cp.ast.nodes.AstNode;

public class FunctionNotDefinedException extends CompilationException {

	private static final long serialVersionUID = 1L;
	private final AstNode node;
	private final String name;

	public FunctionNotDefinedException(String name, AstNode node) {
		super(String.format("Function has not been defined: %s", name));
		this.name = name;
		this.node = node;
	}

	public AstNode getNode() {
		return node;
	}

	public String getName() {
		return name;
	}

}
