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
	FUN("fun"),
	OUT("out"),
	COLON(":"),
	COMMA(":"),
	SEMICOLON(";"),
	IF("if"),
	ELSE("else"),
	EQUALS("=");

	@SuppressWarnings("unused")
	private String name;

	private Token() {
		this(null);
	}

	private Token(String name) {
		this.name = name;
	}

}
