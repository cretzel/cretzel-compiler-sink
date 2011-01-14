package com.cp.ast.nodes;

public abstract class AstNodeImpl implements AstNode {

	public static final int ERRONEOUS = -1;

	public static final int TOPLEVEL = 1;
	public static final int PLUS = TOPLEVEL + 1;
	public static final int MINUS = PLUS + 1;
	public static final int MULT = MINUS + 1;
	public static final int DIV = MULT + 1;
	public static final int IDENTIFIER = DIV + 1;
	public static final int NUMBER_LITERAL = IDENTIFIER + 1;
	public static final int PARENTHESIZED = NUMBER_LITERAL + 1;
	public static final int DECLARATIONS = PARENTHESIZED + 1;
	public static final int DECLARATION = DECLARATIONS + 1;
	public static final int ASSIGNMENT = DECLARATION + 1;
	public static final int OUTPUT = ASSIGNMENT + 1;


	public abstract int getTag();

}
