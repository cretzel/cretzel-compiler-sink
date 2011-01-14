package com.cp;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_test() throws Exception {
	}

	@Test
	public void test_arithmetic_expr_01() throws Exception {

		String input = "5 + 4";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals("5", lexer.lexval());
		Assert.assertEquals(Token.PLUS, lexer.nextToken());
		Assert.assertEquals(null, lexer.lexval());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals("4", lexer.lexval());
		Assert.assertEquals(Token.EOF, lexer.nextToken());

	}

	@Test
	public void test_arithmetic_expr_02() throws Exception {

		String input = "55 + 44";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Token token = lexer.nextToken();
		Assert.assertEquals(Token.NUM, token);
		Assert.assertEquals("55", lexer.lexval());
		Assert.assertEquals(Token.PLUS, lexer.nextToken());
		Assert.assertEquals(null, lexer.lexval());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals("44", lexer.lexval());
		Assert.assertEquals(Token.EOF, lexer.nextToken());

	}

	@Test
	public void test_arithmetic_expr_03() throws Exception {

		String input = "55+ 44";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Token token = lexer.nextToken();
		Assert.assertEquals(Token.NUM, token);
		Assert.assertEquals("55", lexer.lexval());

		token = lexer.nextToken();
		Assert.assertEquals(Token.PLUS, token);
		
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.EOF, lexer.nextToken());

	}

	@Test
	public void test_arithmetic_expr_04() throws Exception {

		String input = "55+44";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.PLUS, lexer.nextToken());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.EOF, lexer.nextToken());

	}

	@Test
	public void test_arithmetic_expr_05() throws Exception {

		String input = "\t 55   +\n  44";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.PLUS, lexer.nextToken());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.EOF, lexer.nextToken());
	}

	@Test
	public void test_arithmetic_expr_06() throws Exception {

		String input = "42 * (17 + 7)";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.MULT, lexer.nextToken());
		Assert.assertEquals(Token.LPAREN, lexer.nextToken());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.PLUS, lexer.nextToken());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.RPAREN, lexer.nextToken());
		Assert.assertEquals(Token.EOF, lexer.nextToken());

	}

	@Test
	public void test_arithmetic_expr_07() throws Exception {

		String input = "42 * (17 + 7)";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);
		
		Assert.assertEquals("NUM MULT LPAREN NUM PLUS NUM RPAREN",
				lexer.readFullyTokenString());

	}
	
	@Test
	public void test_arithmetic_expr_08() throws Exception {

		String input = "42 * _foo";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.MULT, lexer.nextToken());
		Assert.assertEquals(Token.ID, lexer.nextToken());
		Assert.assertEquals(Token.EOF, lexer.nextToken());

	}

	@Test
	public void test_arithmetic_expr_09() throws Exception {

		String input = "val x := 42 + 7";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Assert.assertEquals(Token.VAL, lexer.nextToken());
		Assert.assertEquals(Token.ID, lexer.nextToken());
		Assert.assertEquals(Token.ASSIGNMENT, lexer.nextToken());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.PLUS, lexer.nextToken());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.EOF, lexer.nextToken());

	}

	@Test
	public void test_arithmetic_expr_10() throws Exception {

		String input = "val x := 42 + 7\nout x";
		Reader reader = new BufferedReader(new StringReader(input));

		Lexer lexer = new Lexer(reader);

		Assert.assertEquals(Token.VAL, lexer.nextToken());
		Assert.assertEquals(Token.ID, lexer.nextToken());
		Assert.assertEquals(Token.ASSIGNMENT, lexer.nextToken());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.PLUS, lexer.nextToken());
		Assert.assertEquals(Token.NUM, lexer.nextToken());
		Assert.assertEquals(Token.OUT, lexer.nextToken());
		Assert.assertEquals(Token.ID, lexer.nextToken());
		Assert.assertEquals(Token.EOF, lexer.nextToken());

	}

	@After
	public void tearDown() throws Exception {
	}

}
