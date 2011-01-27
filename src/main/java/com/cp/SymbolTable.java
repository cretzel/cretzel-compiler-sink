package com.cp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.cp.ast.AstAnnotations;
import com.cp.ast.AstAnnotations.AnnotationType;
import com.cp.ast.nodes.AstNode;
import com.cp.ast.nodes.FunctionDeclarationAstNode;
import com.cp.ast.nodes.MainAstNode;
import com.cp.exception.VariableAlreadyDefinedException;
import com.cp.exception.VariableNotDefinedException;

public class SymbolTable {

	public static class SymbolTableEntry {
		public AstNode astNode;
		public int variableNumber;
		public int level;

		private SymbolTableEntry(AstNode astNode, int variableNumber, int level) {
			this.variableNumber = variableNumber;
			this.astNode = astNode;
			this.level = level;
		}

	}

	private static final int TOP_LEVEL = 0;
	private static final int FIRST_VARIABLE_NUMBER = 0;

	private int currentLevel = TOP_LEVEL;
	private int nextVariableNumber = FIRST_VARIABLE_NUMBER;
	private Map<String, SymbolTableEntry> variables;
	private final AstAnnotations annotations;
	private FunctionDeclarationAstNode currentFunction;

	SymbolTable(AstAnnotations annotations) {
		this.annotations = annotations;
		variables = new HashMap<String, SymbolTableEntry>();
	}

	public void enter(String name, AstNode node) {

		if (variables.containsKey(name)) {
			throw new VariableAlreadyDefinedException(name, node);
		}

		SymbolTableEntry entry = makeEntry(name, node);
		variables.put(name, entry);
		annotations.set(node, AnnotationType.VARIABLE_NUMBER,
				nextVariableNumber++);

	}

	private SymbolTableEntry makeEntry(String name, AstNode node) {
		return new SymbolTableEntry(node, nextVariableNumber, currentLevel);
	}

	public SymbolTableEntry get(String name) {
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

	public void leftMain(MainAstNode main) {
		annotations.set(main, AnnotationType.NUMBER_OF_VARIABLES,
				nextVariableNumber - 1);
	}

	public void enterMethod(FunctionDeclarationAstNode function) {
		this.currentFunction = function;
		nextVariableNumber = FIRST_VARIABLE_NUMBER;
	}

	public void enterScope() {
		currentLevel++;
	}

	public void leaveMethod() {
		annotations.set(this.currentFunction,
				AnnotationType.NUMBER_OF_VARIABLES, nextVariableNumber);

		this.currentFunction = null;
		// We are in Main, locals start at 1
		nextVariableNumber = FIRST_VARIABLE_NUMBER + 1;
	}

	public void leaveScope() {

		Iterator<Entry<String, SymbolTableEntry>> tableEntriesIterator = variables
				.entrySet().iterator();
		while (tableEntriesIterator.hasNext()) {
			Entry<String, SymbolTableEntry> entry = tableEntriesIterator.next();
			if (entry.getValue().level == currentLevel) {
				tableEntriesIterator.remove();
			}
		}

		currentLevel--;
	}
}
