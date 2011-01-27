package com.cp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Test;

import util.Utils;

import com.cp.ast.AstAnnotations;
import com.cp.ast.nodes.ProgramAstNode;

public class JasminByteCodeCreatorTest {

	@Test
	public void test_create_01() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 12\nout a"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("12", output);
	}

	@Test
	public void test_create_02() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 12 + 7 * 31\nout a"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("229", output);
	}

	@Test
	public void test_create_03() throws Exception {

		Lexer lexer = new Lexer(new StringReader(
				"val a := 12 + 7 * 31\nout a+1"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("230", output);
	}

	@Test
	public void test_create_04() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 10*10\n"
				+ "val b := 4\n" + "out a/b"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("25", output);
	}

	@Test
	public void test_create_05() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 2\n"
				+ "val b := 3\n" + "val c := 4\n" + "out a*a + b*b + c*c"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("29", output);
	}

	@Test
	public void test_create_06() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 5\n"
				+ "val b := a*a\n" + "out b"));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("25", output);
	}

	@Test
	public void test_fun_01() throws Exception {

		String input = "fun foo(a,b,c):\n" + "a+b+c;\n" + "val x := 1"
				+ "out x";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);
		Assert.assertTrue(jasminSource
				.contains(".method public static foo(III)I"));
		Assert.assertTrue(jasminSource.contains("return"));

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("1", output);
	}

	@Test
	public void test_call_fun_01() throws Exception {

		String input = "fun foo(a,b,c):\n" + "a+b+c;\n" + "val x := foo(1,2,3)"
				+ "out x";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);
		Assert.assertTrue(jasminSource.contains("invokestatic Main/foo(III)I"));

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("6", output);
	}

	@Test
	public void test_call_fun_02() throws Exception {

		String input = "fun foo(a,b,c):\n" + "a+b+c;\n" + "out foo(10,10,22)";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);
		Assert.assertTrue(jasminSource.contains("invokestatic Main/foo(III)I"));

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("42", output);
	}

	@Test
	public void test_call_fun_03() throws Exception {

		String input = "fun foo(a,b,c):a+b+c;\n" + "fun bar(a):\n" + "a*a;\n"
				+ "out bar(foo(1,2,3))";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);
		Assert.assertTrue(jasminSource.contains("invokestatic Main/foo(III)I"));

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("36", output);
	}

	@Test
	public void test_if_else_expression_01() throws Exception {

		String input = "val d := if 1:\n" + "    3;\n" + "else:\n" + "    7;\n"
				+ "out d";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);
		Assert.assertTrue(jasminSource.contains("ifeq"));
		Assert.assertTrue(jasminSource.contains("goto"));

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("3", output);
	}

	@Test
	public void test_recursive_fun_01() throws Exception {

		String input = "fun fak(n):\n" + "if n-1: n * fak(n-1);\n"
				+ "else: 1;\n;" + "out fak(5)";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();
		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();

		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);
		String jasminSource = creator.getSrc();
		System.err.println(jasminSource);

		String output = createByteCodeAndExecuteMain(creator);
		Assert.assertEquals("120", output);
	}

	// Helpers ----------------------------------------------------

	private String createByteCodeAndExecuteMain(JasminByteCodeCreator creator)
			throws FileNotFoundException, IOException, Exception {
		File file = createByteCode(creator);
		String output = Utils.executeMainMethod(file);
		return output;
	}

	private File createByteCode(JasminByteCodeCreator creator)
			throws FileNotFoundException, IOException, Exception {
		File tmpFolder = Utils.createTmpFolder();
		File file = new File(tmpFolder, "Main.class");
		FileOutputStream fout = new FileOutputStream(file);
		creator.createByteCode(fout);
		System.err.println(file.getAbsolutePath());
		return file;
	}

}
