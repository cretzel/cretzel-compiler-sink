package com.cp.exception;

public class UnrecognizedInputException extends CompilationException {

	private static final long serialVersionUID = 1L;
	private final String input;
	private final int lineno;

	public UnrecognizedInputException(String input, int lineno) {
		super("Unrecognized input '" + input + "' on line " + lineno);
		this.input = input;
		this.lineno = lineno;
	}

	public UnrecognizedInputException(char input, int lineno) {
		this("" + input, lineno);
	}

	public String getInput() {
		return input;
	}

	public int getLineno() {
		return lineno;
	}

}
