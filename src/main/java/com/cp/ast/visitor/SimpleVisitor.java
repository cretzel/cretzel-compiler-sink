package com.cp.ast.visitor;

import com.cp.ast.nodes.AssignmentAstNode;
import com.cp.ast.nodes.BinaryAstNode;
import com.cp.ast.nodes.DeclarationAstNode;
import com.cp.ast.nodes.DeclarationsAstNode;
import com.cp.ast.nodes.ErroneousAstNode;
import com.cp.ast.nodes.IdentifierAstNode;
import com.cp.ast.nodes.NumberLiteralAstNode;
import com.cp.ast.nodes.OutputAstNode;
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

	void visitOutput(OutputAstNode outputAstNodeImpl);

}
