package com.cp;

import java.io.PrintWriter;
import java.io.StringReader;

import org.junit.Test;

import com.cp.ast.nodes.ProgramAstNode;
import com.cp.ast.visitor.PrettyPrintVisitor;

public class ParserTest {

	@Test
	public void test_parse_01() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 12 + 4"));
		Parser parser = new Parser(lexer);

		parser.parseFully();

	}

	@Test
	public void test_parse_02() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a:= 12 + 5 + 4 * 55"));
		Parser parser = new Parser(lexer);

		parser.parseFully();
	}

	@Test
	public void test_parse_03() throws Exception {

		Lexer lexer = new Lexer(
				new StringReader("val a := 12 + ((5 + 4)) * 55"));
		Parser parser = new Parser(lexer);

		parser.parseFully();
	}

	@Test
	public void test_parse_04() throws Exception {

		Lexer lexer = new Lexer(new StringReader(
				"val foo := 12 + 55 * 10 * (5 + 1)"));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_05() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val foo := 12 + 4\n"
				+ "val bar := foo + 12"));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_07() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 12\n"
				+ "val b := 14\n" + "val c:=a + b"));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_08() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 12\n" + "out a"));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_09() throws Exception {

		Lexer lexer = new Lexer(new StringReader("val a := 12\n"
				+ "val b := a\n" + "out a + b"));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_fun_01() throws Exception {

		String input = "fun foo(x):\n" + "4;\n" + "val a := 1" + "out a";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_fun_02() throws Exception {

		String input = "fun foo():\n" + "4+5;\n" + "val a := 1" + "out a";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_fun_03() throws Exception {

		String input = "fun foo(a,b,c):\n" + "a+b+c;\n" + "val x := 1"
				+ "out x";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_call_fun_01() throws Exception {

		String input = "fun foo(a,b,c):\n" + "a+b+c;\n" + "val x := foo(1,2,3)"
				+ "out x";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	@Test
	public void test_parse_fun_04() throws Exception {

		String input = "fun foo(a,b,c):\n" + "val d:=a*b*c\n" + "a+b+c+d;\n"
				+ "val x := 1" + "out x";
		Lexer lexer = new Lexer(new StringReader(input));
		Parser parser = new Parser(lexer);

		ProgramAstNode ast = parser.parseFully();

		prettyPrint(ast);
	}

	private void prettyPrint(ProgramAstNode ast) {
		PrintWriter pw = new PrintWriter(System.out);
		PrettyPrintVisitor visitor = new PrettyPrintVisitor(pw);
		ast.accept(visitor);
		pw.flush();
	}

}
