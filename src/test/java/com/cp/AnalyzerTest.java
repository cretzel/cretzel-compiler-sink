package com.cp;

import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Test;

import com.cp.Analyzer;
import com.cp.Lexer;
import com.cp.Parser;
import com.cp.SymbolTable;
import com.cp.SymbolTable.SymbolTableEntry;
import com.cp.ast.AstAnnotations;
import com.cp.ast.AstAnnotations.AnnotationType;
import com.cp.ast.nodes.ProgramAstNode;

public class AnalyzerTest {

	@Test
	public void test_analyze_01() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 12 + 4"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();

		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		SymbolTable symtable = analyzer.getSymbolTable();
		Assert.assertTrue(symtable.hasEntry("a"));
		SymbolTableEntry entry = symtable.get("a");
		Assert.assertTrue(entry.variableNumber == 0);

		AstAnnotations annotations = analyzer.getAnnotations();
		Object varNumber = annotations.get(entry.astNode,
				AnnotationType.VARIABLE_NUMBER);
		Assert.assertNotNull(varNumber);
		Assert.assertEquals(0, varNumber);
	}

	@Test
	public void test_analyze_fun_01() throws Exception {

		String input = "fun foo(a,b,c):\n" + "a+b+c;\n" + "val x := 1"
				+ "out x";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();

		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		SymbolTable symtable = analyzer.getSymbolTable();
		Assert.assertFalse(symtable.hasEntry("a"));
		Assert.assertFalse(symtable.hasEntry("b"));
		Assert.assertFalse(symtable.hasEntry("c"));
		Assert.assertTrue(symtable.hasEntry("x"));
		SymbolTableEntry entry = symtable.get("x");
		// 1,2,3 have been given to a,b,c, after function set back to 0
		Assert.assertTrue(entry.variableNumber == 1);

	}

}
