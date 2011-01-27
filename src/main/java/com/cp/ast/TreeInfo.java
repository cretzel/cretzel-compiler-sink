package com.cp.ast;

import com.cp.ast.nodes.AstNode.Kind;
import com.cp.ast.nodes.AstNodeImpl;


public class TreeInfo {

	public static Kind tagToKind(int tag) {
		switch (tag) {
		case AstNodeImpl.DIV:
			return Kind.DIVIDE;
		case AstNodeImpl.MULT:
			return Kind.MULT;
		case AstNodeImpl.PLUS:
			return Kind.ADD;
		case AstNodeImpl.MINUS:
			return Kind.SUBTRACT;
		default:
			throw new RuntimeException("Unknown tag: " + tag);
		}
	}
}
