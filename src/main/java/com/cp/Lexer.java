package com.cp;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.cp.exception.UnrecognizedInputException;

public class Lexer {

	private final Reader reader;
	private int lineno = 1;
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
		case '\r':
		case '\t':
			read();
			return nextToken();
		case '\n':
			lineno++;
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
			return parseAssignmentOrColon();
		case '=':
			read();
			return Token.EQUALS;
		case ';':
			read();
			return Token.SEMICOLON;
		case ',':
			read();
			return Token.COMMA;

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

		throw new UnrecognizedInputException((char) current, lineno);
	}

	private Token parseAssignmentOrColon() {
		if (lookahead == '=') {
			read();
			read();
			return Token.ASSIGNMENT;
		} else {
			read();
			return Token.COLON;
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
		if ("fun".equals(this.lexval)) {
			return Token.FUN;
		}
		if ("out".equals(this.lexval)) {
			return Token.OUT;
		}
		if ("if".equals(this.lexval)) {
			return Token.IF;
		}
		if ("else".equals(this.lexval)) {
			return Token.ELSE;
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
