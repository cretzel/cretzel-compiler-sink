package com.cp.ast.visitor;

import com.cp.ast.nodes.AssignmentAstNode;
import com.cp.ast.nodes.BinaryAstNode;
import com.cp.ast.nodes.BlockAstNode;
import com.cp.ast.nodes.DeclarationAstNode;
import com.cp.ast.nodes.DeclarationsAstNode;
import com.cp.ast.nodes.ErroneousAstNode;
import com.cp.ast.nodes.FunctionDeclarationAstNode;
import com.cp.ast.nodes.FunctionDeclarationsAstNode;
import com.cp.ast.nodes.FunctionInvocationAstNode;
import com.cp.ast.nodes.IdentifierAstNode;
import com.cp.ast.nodes.IfElseAstNode;
import com.cp.ast.nodes.MainAstNode;
import com.cp.ast.nodes.NumberLiteralAstNode;
import com.cp.ast.nodes.OutputAstNode;
import com.cp.ast.nodes.ParameterAstNode;
import com.cp.ast.nodes.ParenthesizedAstNode;
import com.cp.ast.nodes.ProgramAstNode;

public interface SimpleVisitor {

	void visitBinary(BinaryAstNode binary);

	void visitErroneous(ErroneousAstNode erroneous);

	void visitIdentifier(IdentifierAstNode identifier);

	void visitNumberLiteral(NumberLiteralAstNode numberLiteral);

	void visitParenthesized(ParenthesizedAstNode parenthesized);

	void visitProgram(ProgramAstNode program);

	void visitDeclarations(DeclarationsAstNode declrs);

	void visitDeclaration(DeclarationAstNode declr);

	void visitAssignment(AssignmentAstNode assignment);

	void visitOutput(OutputAstNode output);

	void visitBlock(BlockAstNode bloc);

	void visitParameter(ParameterAstNode parameter);

	void visitFunctionDeclaration(FunctionDeclarationAstNode functionDeclaration);

	void visitFunctionDeclarations(
			FunctionDeclarationsAstNode functionDeclarations);

	void visitMain(MainAstNode main);

	void visitFunctionInvocation(FunctionInvocationAstNode functionInvocation);

	void visitIfElse(IfElseAstNode ifElse);

}
