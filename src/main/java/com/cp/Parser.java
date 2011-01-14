package com.cp;

import com.cp.ast.nodes.AssignmentAstNode;
import com.cp.ast.nodes.AssignmentAstNodeImpl;
import com.cp.ast.nodes.AstNodeImpl;
import com.cp.ast.nodes.BinaryAstNodeImpl;
import com.cp.ast.nodes.DeclarationAstNode;
import com.cp.ast.nodes.DeclarationsAstNode;
import com.cp.ast.nodes.DeclarationsAstNodeImpl;
import com.cp.ast.nodes.ErroneousAstNodeImpl;
import com.cp.ast.nodes.ExpressionAstNode;
import com.cp.ast.nodes.IdentifierAstNode;
import com.cp.ast.nodes.IdentifierAstNodeImpl;
import com.cp.ast.nodes.NumberLiteralAstNodeImpl;
import com.cp.ast.nodes.OutputAstNode;
import com.cp.ast.nodes.OutputAstNodeImpl;
import com.cp.ast.nodes.ParenthesizedAstNodeImpl;
import com.cp.ast.nodes.ProgramAstNode;
import com.cp.ast.nodes.ProgramAstNodeImpl;
import static com.cp.Token.*;

public class Parser {

	private final Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
		lexer.nextToken();
	}

	private boolean accept(Token token) {
		if (lexer.token() == token) {
			lexer.nextToken();
			return true;
		} else {
			return false;
		}
	}

	private boolean expect(Token token) {
		if (accept(token)) {
			return true;
		} else {
			reportError("expect: unexpected symbol: " + token);
			return false;
		}
	}

	public ProgramAstNode parseFully() {
		return program();
	}

	/**
	 * programm -> declarations [output] EOF
	 * 
	 */
	private ProgramAstNode program() {
		DeclarationsAstNode declrAstNode = declarations();
		OutputAstNode outputAstNode = output();
		expect(EOF);
		return new ProgramAstNodeImpl(declrAstNode, outputAstNode);
	}

	private OutputAstNode output() {

		while (lexer.token() == Token.OUT) {
			expect(Token.OUT);
			ExpressionAstNode exprAstNode = expr();
			OutputAstNode outAstNode = new OutputAstNodeImpl(exprAstNode);
			return outAstNode;
		}

		return null;
	}

	/**
	 * declarations-> declaration declarations -> eps
	 * 
	 * @return
	 */
	private DeclarationsAstNode declarations() {
		DeclarationsAstNode declrAstNode = new DeclarationsAstNodeImpl();

		while (lexer.token() == VAL) {
			DeclarationAstNode declaration = declaration();
			declrAstNode.addDeclaration(declaration);
		}

		return declrAstNode;

	}

	/**
	 * declaration -> ID ':=' expr
	 */
	public DeclarationAstNode declaration() {

		expect(VAL);
		String lexval = lexer.lexval();
		accept(ID);
		IdentifierAstNode idAstNode = new IdentifierAstNodeImpl(lexval);
		expect(ASSIGNMENT);
		ExpressionAstNode exprAstNode = expr();

		AssignmentAstNode assignmentAstNode = new AssignmentAstNodeImpl(
				idAstNode, exprAstNode);
		return assignmentAstNode;
	}

	/**
	 * expr -> term {[+|-] term}
	 * 
	 * @return ExpressionAstNode
	 */
	private ExpressionAstNode expr() {
		ExpressionAstNode termAstNode = term();

		while (lexer.token() == PLUS || lexer.token() == MINUS) {
			if (lexer.token() == PLUS) {
				expect(PLUS);
				ExpressionAstNode termAstNode1 = term();
				termAstNode = new BinaryAstNodeImpl(AstNodeImpl.PLUS,
						termAstNode, termAstNode1);
			} else if (lexer.token() == MINUS) {
				expect(MINUS);
				ExpressionAstNode termAstNode1 = term();
				termAstNode = new BinaryAstNodeImpl(AstNodeImpl.MINUS,
						termAstNode, termAstNode1);
			} else {
				reportError("Cannot happen");
			}
		}

		return termAstNode;

	}

	/**
	 * term -> factor {[+|-] factor}
	 * 
	 * @return
	 */
	private ExpressionAstNode term() {

		ExpressionAstNode lhs = factor();

		while (lexer.token() == MULT || lexer.token() == DIV) {
			if (lexer.token() == MULT) {
				accept(MULT);
				ExpressionAstNode rhs = factor();
				lhs = new BinaryAstNodeImpl(AstNodeImpl.MULT, lhs, rhs);
			} else if (lexer.token() == DIV) {
				accept(DIV);
				ExpressionAstNode rhs = factor();
				lhs = new BinaryAstNodeImpl(AstNodeImpl.DIV, lhs, rhs);
			} else {
				reportError("Cannot happen");
				return new ErroneousAstNodeImpl();
			}
		}

		return lhs;
	}

	/**
	 * factor -> LPAREN expr RPAREN
	 * 
	 * factor -> ID
	 * 
	 * factor -> NUM
	 * 
	 * @return
	 */
	private ExpressionAstNode factor() {
		if (accept(LPAREN)) {
			ExpressionAstNode exprAstNode = expr();
			expect(RPAREN);
			return new ParenthesizedAstNodeImpl(exprAstNode);
		} else if (lexer.token() == ID) {
			String lexval = lexer.lexval();
			accept(ID);
			return new IdentifierAstNodeImpl(lexval);
		} else if (lexer.token() == NUM) {
			String lexval = lexer.lexval();
			accept(NUM);
			return new NumberLiteralAstNodeImpl(lexval);
		}

		reportError("Expected (expr) or identifier or number, but was "
				+ lexer.token());
		return new ErroneousAstNodeImpl();
	}

	private void reportError(String msg) {
		throw new RuntimeException(msg);
	}
}
