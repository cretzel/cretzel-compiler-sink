package com.cp;

import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Test;

import com.cp.Analyzer;
import com.cp.Lexer;
import com.cp.Parser;
import com.cp.SymbolTable;
import com.cp.SymbolTable.Entry;
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
		Entry entry = symtable.get("a");
		Assert.assertTrue(entry.variableNumber == 1);
		
		AstAnnotations annotations = analyzer.getAnnotations();
		Object varNumber = annotations.get(entry.astNode, AnnotationType.VARIABLE_NUMBER);
		Assert.assertNotNull(varNumber);
		Assert.assertEquals(1, varNumber);
	}

}