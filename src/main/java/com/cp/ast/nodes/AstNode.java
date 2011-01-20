package com.cp.ast.nodes;

import com.cp.ast.visitor.SimpleVisitor;

public interface AstNode {

	public enum Kind {

		PROGRAM(ProgramAstNode.class),
		EXPRESSION(ExpressionAstNode.class),
		DIVIDE(BinaryAstNode.class),
		MULT(BinaryAstNode.class),
		ADD(BinaryAstNode.class),
		SUBTRACT(BinaryAstNode.class),
		PARENTHESIZED(ParenthesizedAstNode.class),
		IDENTIFIER(IdentifierAstNode.class),
		NUMBER_LITERAL(NumberLiteralAstNode.class),
		ERRONEOUS(ErroneousAstNode.class),
		DECLARATION(DeclarationAstNode.class),
		DECLARATIONS(DeclarationsAstNode.class),
		ASSIGNMENT(AssignmentAstNode.class),
		FUNCTIONDECLARATION(FunctionDeclarationAstNode.class),
		OUTPUT(OutputAstNode.class),
		PARAMETER(ParameterAstNode.class),
		BLOCK(BlockAstNode.class),
		FUNCTION_DECLARATIONS(FunctionDeclarationAstNode.class),
		MAIN(MainAstNode.class);

		private final Class<? extends AstNode> associatedInterface;

		Kind(Class<? extends AstNode> intf) {
			associatedInterface = intf;
		}

		public Class<? extends AstNode> asInterface() {
			return associatedInterface;
		}

	}

	Kind getKind();

	void accept(SimpleVisitor visitor);

	int getTag();

}
