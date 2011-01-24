package com.cp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import com.cp.ast.AstAnnotations;
import com.cp.ast.nodes.ProgramAstNode;
import com.cp.util.FileUtils;

public class Compiler {

	public void compile(File srcFile) throws IOException, Exception {
		String input = FileUtils.readFile(srcFile);

		Lexer lexer = new Lexer(new StringReader(input));

		Parser parser = new Parser(lexer);
		ProgramAstNode ast = parser.parseFully();

		Analyzer analyzer = new Analyzer();
		ast.accept(analyzer);

		AstAnnotations annotations = analyzer.getAnnotations();
		JasminByteCodeCreator creator = new JasminByteCodeCreator(annotations);
		ast.accept(creator);

		String outFolder = srcFile.getParent();
		String srcName = srcFile.getName();
		String srcBaseName = srcName.substring(0, srcName.lastIndexOf('.'));
		String outName = /*srcBaseName*/ "Main" + ".class";
		File outFile = new File(outFolder, outName);
		FileOutputStream fout = new FileOutputStream(outFile);
		creator.createByteCode(fout);
	}

	public static void main(String[] args) {
		File srcFile = new File(args[0]);

		Compiler compiler = new Compiler();

		try {
			compiler.compile(srcFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
