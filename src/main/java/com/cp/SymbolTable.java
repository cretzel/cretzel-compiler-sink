package com.cp;

import java.util.HashMap;
import java.util.Map;

import com.cp.ast.AstAnnotations;
import com.cp.ast.AstAnnotations.AnnotationType;
import com.cp.ast.nodes.AstNode;
import com.cp.ast.nodes.ProgramAstNode;
import com.cp.exception.VariableAlreadyDefinedException;
import com.cp.exception.VariableNotDefinedException;

public class SymbolTable {

	public static class Entry {
		public AstNode astNode;
		public int variableNumber;

		private Entry(AstNode astNode, int variableNumber) {
			this.variableNumber = variableNumber;
			this.astNode = astNode;
		}

	}

	private Map<String, Entry> variables;
	private int nextVariableNumber = 1;
	private final AstAnnotations annotations;

	SymbolTable(AstAnnotations annotations) {
		this.annotations = annotations;
		variables = new HashMap<String, Entry>();
	}

	public void enter(String name, AstNode node) {

		if (variables.containsKey(name)) {
			throw new VariableAlreadyDefinedException(name, node);
		}

		Entry entry = makeEntry(name, node);
		variables.put(name, entry);
		annotations.set(node, AnnotationType.VARIABLE_NUMBER,
				nextVariableNumber++);

	}

	private Entry makeEntry(String name, AstNode node) {
		return new Entry(node, nextVariableNumber);
	}

	public Entry get(String name) {
		return variables.get(name);
	}

	public boolean hasEntry(String name) {
		return variables.containsKey(name);
	}

	public void variableAccess(String name, AstNode node) {
		if (!hasEntry(name)) {
			throw new VariableNotDefinedException(name, node);
		}

		annotations.set(node, AnnotationType.VARIABLE_NUMBER,
				variables.get(name).variableNumber);
	}

	public void leftDeclarations(ProgramAstNode program) {
		annotations.set(program, AnnotationType.NUMBER_OF_VARIABLES,
				nextVariableNumber - 1);
	}
}
