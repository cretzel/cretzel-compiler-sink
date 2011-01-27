package com.cp.exception;

public class CompilationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CompilationException() {
		super();
	}

	public CompilationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompilationException(String message) {
		super(message);
	}

	public CompilationException(Throwable cause) {
		super(cause);
	}

}
