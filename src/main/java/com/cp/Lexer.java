package com.cp;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

	private final Reader reader;
	private int lookahead = -1;
	private int current = -1;
	private Token token;
	private String lexval;

	public Lexer(Reader reader) {
		this.reader = reader;
		read();
		read();
	}

	private void read() {
		current = lookahead;
		try {
			lookahead = reader.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Token nextToken() {
		token = nextTokenInternal();
		return token;
	}

	public Token token() {
		return token;
	}

	public String lexval() {
		return lexval;
	}

	private Token nextTokenInternal() {
		this.lexval = null;

		switch (current) {

		case ' ':
		case '\t':
		case '\n':
			read();
			return nextToken();

		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return parseNumber();

		case '+':
		case '-':
		case '*':
		case '/':
			return parseOperator();

		case ':':
			return parseAssignment();

		case '(':
			read();
			return Token.LPAREN;
		case ')':
			read();
			return Token.RPAREN;

		}

		if (Character.isJavaIdentifierStart(current)) {
			return parseIdentifier();
		}

		if (current == -1) {
			return Token.EOF;
		}

		throw new RuntimeException("Unrecognized input: '" + (char) current
				+ "'");
	}

	private Token parseAssignment() {
		if (lookahead == '=') {
			read();
			read();
			return Token.ASSIGNMENT;
		} else {
			throw new RuntimeException("Assignment expected");
		}
	}

	private Token parseIdentifier() {
		StringBuilder lexval = new StringBuilder();
		do {
			lexval.append((char) current);
			read();
		} while (Character.isJavaIdentifierPart(current));

		this.lexval = lexval.toString();

		// Keywords
		if ("val".equals(this.lexval)) {
			return Token.VAL;
		}
		if ("out".equals(this.lexval)) {
			return Token.OUT;
		}

		return Token.ID;

	}

	private Token parseOperator() {
		int cc = current;
		read();

		switch (cc) {
		case '+':
			return Token.PLUS;
		case '-':
			return Token.MINUS;
		case '*':
			return Token.MULT;
		case '/':
			return Token.DIV;
		default:
			throw new IllegalStateException("Could not read operator "
					+ ((char) cc));
		}
	}

	private Token parseNumber() {
		StringBuilder lexval = new StringBuilder();

		do {
			lexval.append((char) current);
			read();
		} while (Character.isDigit(current));

		this.lexval = lexval.toString();
		return Token.NUM;
	}

	public List<Token> readFully() {
		List<Token> tokens = new ArrayList<Token>();
		Token token;
		while ((token = nextToken()) != Token.EOF) {
			tokens.add(token);
		}
		return tokens;
	}

	public String readFullyTokenString() {
		StringBuilder builder = new StringBuilder();
		List<Token> tokens = readFully();
		for (Token token : tokens) {
			builder.append(token.name()).append(" ");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

}
