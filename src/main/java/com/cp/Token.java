package com.cp;

public enum Token {

	NUM,
	PLUS("+"),
	MINUS("-"),
	DIV("/"),
	MULT("*"),
	LPAREN("("),
	RPAREN(")"),
	ID,
	EOF,
	ASSIGNMENT(":="),
	VAL("val"),
	OUT("out");

	@SuppressWarnings("unused")
	private String name;

	private Token() {
		this(null);
	}

	private Token(String name) {
		this.name = name;
	}

}
