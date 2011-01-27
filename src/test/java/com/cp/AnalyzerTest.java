package com.cp;

import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Test;

import com.cp.SymbolTable.SymbolTableEntry;
import com.cp.ast.AstAnnotations;
import com.cp.ast.AstAnnotations.AnnotationType;
import com.cp.ast.nodes.ProgramAstNode;
import com.cp.exception.FunctionAlreadyDefinedException;
import com.cp.exception.FunctionNotDefinedException;
import com.cp.exception.FunctionParametersDontMatchException;
import com.cp.exception.VariableAlreadyDefinedException;
import com.cp.exception.VariableNotDefinedException;

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

	@Test(expected = VariableAlreadyDefinedException.class)
	public void test_variableAlreadDefined_Exception() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 2\n"
				+ "val a := 3\n" + "out a+b"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

	}

	@Test(expected = VariableNotDefinedException.class)
	public void test_variableNotDefined_Exception_1() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 2\n" + "out b"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

	}

	@Test(expected = VariableNotDefinedException.class)
	public void test_variableNotDefined_Exception_2() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 2\n"
				+ "val b := a + b\n" + "out b"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

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

	@Test
	public void test_analyze_call_fun_01() throws Exception {

		String input = "fun foo(a,b,c):\n" + "a+b+c;\n" + "val x := foo(1,2,3)"
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

	@Test(expected = FunctionNotDefinedException.class)
	public void test_call_fun_not_defined_01() throws Exception {

		String input = "fun foo(a):\n" + "a;\n" + "out bar(1)";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

	}

	@Test(expected = FunctionAlreadyDefinedException.class)
	public void test_call_fun_already_defined_01() throws Exception {

		String input = "fun foo(a):\na;\n" + "fun foo(x):\nx;\n" + "out foo(1)";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

	}

	@Test(expected = FunctionParametersDontMatchException.class)
	public void test_call_fun_not_defined_params_dont_match_03()
			throws Exception {

		String input = "fun foo(a):\n" + "a;\n" + "out foo(1,2)";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

	}

}
