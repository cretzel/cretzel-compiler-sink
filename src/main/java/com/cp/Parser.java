package com.cp;

import static com.cp.Token.ASSIGNMENT;
import static com.cp.Token.DIV;
import static com.cp.Token.EOF;
import static com.cp.Token.FUN;
import static com.cp.Token.ID;
import static com.cp.Token.IF;
import static com.cp.Token.LPAREN;
import static com.cp.Token.MINUS;
import static com.cp.Token.MULT;
import static com.cp.Token.NUM;
import static com.cp.Token.PLUS;
import static com.cp.Token.RPAREN;
import static com.cp.Token.VAL;

import java.util.ArrayList;
import java.util.List;

import com.cp.ast.nodes.AssignmentAstNode;
import com.cp.ast.nodes.AssignmentAstNodeImpl;
import com.cp.ast.nodes.AstNodeImpl;
import com.cp.ast.nodes.BinaryAstNodeImpl;
import com.cp.ast.nodes.BlockAstNode;
import com.cp.ast.nodes.BlockAstNodeImpl;
import com.cp.ast.nodes.DeclarationAstNode;
import com.cp.ast.nodes.DeclarationsAstNode;
import com.cp.ast.nodes.DeclarationsAstNodeImpl;
import com.cp.ast.nodes.ErroneousAstNodeImpl;
import com.cp.ast.nodes.ExpressionAstNode;
import com.cp.ast.nodes.FunctionDeclarationAstNode;
import com.cp.ast.nodes.FunctionDeclarationAstNodeImpl;
import com.cp.ast.nodes.FunctionDeclarationsAstNode;
import com.cp.ast.nodes.FunctionDeclarationsAstNodeImpl;
import com.cp.ast.nodes.FunctionInvocationAstNodeImpl;
import com.cp.ast.nodes.IdentifierAstNode;
import com.cp.ast.nodes.IdentifierAstNodeImpl;
import com.cp.ast.nodes.IfElseAstNode;
import com.cp.ast.nodes.IfElseAstNodeImpl;
import com.cp.ast.nodes.MainAstNode;
import com.cp.ast.nodes.MainAstNodeImpl;
import com.cp.ast.nodes.NumberLiteralAstNodeImpl;
import com.cp.ast.nodes.OutputAstNode;
import com.cp.ast.nodes.OutputAstNodeImpl;
import com.cp.ast.nodes.ParameterAstNode;
import com.cp.ast.nodes.ParameterAstNodeImpl;
import com.cp.ast.nodes.ParenthesizedAstNodeImpl;
import com.cp.ast.nodes.ProgramAstNode;
import com.cp.ast.nodes.ProgramAstNodeImpl;
import com.cp.exception.CompilationException;
import com.cp.exception.ParseException;

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
			reportError("expected symbol " + token + ", but was "
					+ lexer.token());
			return false;
		}
	}

	public ProgramAstNode parseFully() {
		return program();
	}

	/**
	 * programm -> functionDeclarations main EOF
	 */
	private ProgramAstNode program() {
		FunctionDeclarationsAstNode funDeclrs = functionDeclarations();
		MainAstNode main = main();
		expect(EOF);
		return new ProgramAstNodeImpl(funDeclrs, main);
	}

	private MainAstNode main() {
		DeclarationsAstNode declarations = declarations();
		OutputAstNode output = output();
		return new MainAstNodeImpl(declarations, output);

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
	private FunctionDeclarationsAstNode functionDeclarations() {
		FunctionDeclarationsAstNode declrAstNode = new FunctionDeclarationsAstNodeImpl();

		while (lexer.token() == FUN) {
			declrAstNode.addDeclaration(functionDeclaration());
		}

		return declrAstNode;

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
	 * declaration -> variableDeclaration | functionDeclaration
	 */
	public DeclarationAstNode declaration() {

		if (lexer.token() == VAL) {
			return variableDeclaration();
		} else if (lexer.token() == FUN) {
			return functionDeclaration();
		} else {
			throw new RuntimeException(
					"Expected variable or function declaration");
		}
	}

	/**
	 * variableDeclaration -> ID ':=' expr
	 */
	private AssignmentAstNode variableDeclaration() {
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
	 * functionDeclaration -> FUN ID parameterList COLON block
	 * 
	 * block -> variableDeclarations expr SEMICOLON
	 * 
	 * @return
	 */
	private FunctionDeclarationAstNode functionDeclaration() {

		expect(FUN);

		String funId = lexer.lexval();
		expect(ID);
		IdentifierAstNode idAstNode = new IdentifierAstNodeImpl(funId);

		List<ParameterAstNode> parameters = parameterList();

		expect(Token.COLON);

		BlockAstNode blockAstNode = block();

		FunctionDeclarationAstNode funAstNode = new FunctionDeclarationAstNodeImpl(
				idAstNode, parameters, blockAstNode);

		return funAstNode;
	}

	/**
	 * parameterList -> LPARENT ( RPAREN | parameters RPAREN)
	 */
	private List<ParameterAstNode> parameterList() {
		expect(LPAREN);

		if (lexer.token() == RPAREN) {
			expect(RPAREN);
			return new ArrayList<ParameterAstNode>();
		} else {
			List<ParameterAstNode> parameters = parameters();
			expect(RPAREN);
			return parameters;
		}
	}

	/**
	 * parameters -> ID (',' parameters)*
	 */
	private List<ParameterAstNode> parameters() {
		List<ParameterAstNode> parameters = new ArrayList<ParameterAstNode>();

		String parameterName = lexer.lexval();
		expect(ID);
		ParameterAstNode paramAstNode = new ParameterAstNodeImpl(parameterName);
		parameters.add(paramAstNode);

		while (lexer.token() == Token.COMMA) {
			expect(Token.COMMA);
			parameters.addAll(parameters());
		}

		return parameters;
	}

	/**
	 * block -> variableDeclarations expr SEMICOLON
	 * 
	 * @return
	 */
	private BlockAstNode block() {

		List<AssignmentAstNode> declarations = new ArrayList<AssignmentAstNode>();
		while (lexer.token() == Token.VAL) {
			AssignmentAstNode variableDeclaration = variableDeclaration();
			declarations.add(variableDeclaration);
		}

		ExpressionAstNode expr = expr();

		expect(Token.SEMICOLON);

		BlockAstNode block = new BlockAstNodeImpl(declarations, expr);
		return block;
	}

	/**
	 * expr -> ifExpression
	 * 
	 * expr -> term {[+|-] term}
	 */
	private ExpressionAstNode expr() {

		if (lexer.token() == IF) {
			return ifElseExpression();
		} else {
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
	}

	/**
	 * ifExpression -> 'if' expr ':' block 'else' ':' block
	 */
	private IfElseAstNode ifElseExpression() {

		if (accept(IF)) {
			ExpressionAstNode condition = expr();
			expect(Token.COLON);
			BlockAstNode ifBlock = block();
			expect(Token.ELSE);
			expect(Token.COLON);
			BlockAstNode elseBlock = block();
			return new IfElseAstNodeImpl(condition, ifBlock, elseBlock);
		} else {
			throw new CompilationException("Cannot happen");
		}
	}

	/**
	 * term -> factor {[*|/] factor}
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
	 * factor -> ID ( '()' | '(' arguments + ')' )?
	 * 
	 * factor -> NUM
	 */
	private ExpressionAstNode factor() {
		if (accept(LPAREN)) {
			ExpressionAstNode exprAstNode = expr();
			expect(RPAREN);
			return new ParenthesizedAstNodeImpl(exprAstNode);
		} else if (lexer.token() == ID) {
			String name = lexer.lexval();
			accept(ID);

			if (lexer.token() == LPAREN) {
				accept(LPAREN);
				List<ExpressionAstNode> arguments = lexer.token() == RPAREN ? new ArrayList<ExpressionAstNode>()
						: arguments();
				expect(RPAREN);
				return new FunctionInvocationAstNodeImpl(name, arguments);
			} else {
				return new IdentifierAstNodeImpl(name);
			}

		} else if (lexer.token() == NUM) {
			String lexval = lexer.lexval();
			accept(NUM);
			return new NumberLiteralAstNodeImpl(lexval);
		}

		reportError("Expected (expr) or identifier or number, but was "
				+ lexer.token());
		return new ErroneousAstNodeImpl();
	}

	/**
	 * arguments -> expr ( ',' expr )*
	 */
	private List<ExpressionAstNode> arguments() {
		List<ExpressionAstNode> arguments = new ArrayList<ExpressionAstNode>();

		arguments.add(expr());

		while (accept(Token.COMMA)) {
			arguments.add(expr());
		}

		return arguments;
	}

	private void reportError(String msg) {
		throw new ParseException(msg);
	}
}
