package com.cp.ast.visitor;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.cp.ast.nodes.AssignmentAstNode;
import com.cp.ast.nodes.BinaryAstNode;
import com.cp.ast.nodes.BlockAstNode;
import com.cp.ast.nodes.DeclarationAstNode;
import com.cp.ast.nodes.DeclarationsAstNode;
import com.cp.ast.nodes.ErroneousAstNode;
import com.cp.ast.nodes.FunctionDeclarationAstNode;
import com.cp.ast.nodes.FunctionDeclarationsAstNode;
import com.cp.ast.nodes.IdentifierAstNode;
import com.cp.ast.nodes.MainAstNode;
import com.cp.ast.nodes.NumberLiteralAstNode;
import com.cp.ast.nodes.OutputAstNode;
import com.cp.ast.nodes.ParameterAstNode;
import com.cp.ast.nodes.ParenthesizedAstNode;
import com.cp.ast.nodes.ProgramAstNode;

public class PrettyPrintVisitor implements SimpleVisitor {

	private final Writer out;

	/** Current left margin */
	private int lmargin = 0;

	/** Indentation width */
	private int width = 4;

	private String lineSep = System.getProperty("line.separator");

	public PrettyPrintVisitor(Writer out) {
		this.out = out;
	}

	public void println() throws IOException {
		out.write(lineSep);
	}

	void indent() {
		lmargin = lmargin + width;
	}

	/**
	 * Decrease left margin by indentation width.
	 */
	void undent() {
		lmargin = lmargin - width;
	}

	void align() throws IOException {
		for (int i = 0; i < lmargin; i++)
			out.write(" ");
	}

	@Override
	public void visitBinary(BinaryAstNode binary) {

		try {
			align();
			out.write(binary.getKind().name());
			println();
			indent();
			binary.getLhs().accept(this);
			binary.getRhs().accept(this);
			undent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitErroneous(ErroneousAstNode erroneous) {
		try {
			align();
			out.write(erroneous.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitIdentifier(IdentifierAstNode identifier) {
		try {
			align();
			out.write(String.format("ID [%s]", identifier.getName()));
			println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitNumberLiteral(NumberLiteralAstNode numberLiteral) {
		try {
			align();
			out.write(String.format("NUM [%s]", numberLiteral.getValue()));
			println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitParenthesized(ParenthesizedAstNode parenthesized) {
		try {
			align();
			out.write(parenthesized.getKind().name());
			println();
			indent();
			parenthesized.getExpr().accept(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitProgram(ProgramAstNode program) {
		try {
			align();
			out.write(program.getKind().name());
			println();
			indent();

			program.getFunctionDeclarations().accept(this);
			program.getMain().accept(this);

			undent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitFunctionDeclarations(
			FunctionDeclarationsAstNode functionDeclarations) {

		List<FunctionDeclarationAstNode> declarations = functionDeclarations
				.getDeclarations();
		for (FunctionDeclarationAstNode function : declarations) {
			function.accept(this);

		}
	}

	@Override
	public void visitMain(MainAstNode main) {
		try {
			align();
			out.write(main.getKind().name());
			println();
			indent();

			main.getDeclr().accept(this);

			OutputAstNode output = main.getOutput();
			if (output != null) {
				output.accept(this);
			}
			undent();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void visitDeclaration(DeclarationAstNode declr) {
		throw new UnsupportedOperationException();

		// try {
		// align();
		// out.write(declr.getKind().name());
		// println();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void visitDeclarations(DeclarationsAstNode declrs) {
		try {
			align();
			out.write(declrs.getKind().name());
			println();
			indent();
			for (DeclarationAstNode declr : declrs.getDeclarations()) {
				declr.accept(this);
			}
			undent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitAssignment(AssignmentAstNode assignment) {
		try {
			align();
			out.write(assignment.getKind().name());
			println();
			indent();
			assignment.getId().accept(this);
			assignment.getExpr().accept(this);
			undent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitFunctionDeclaration(FunctionDeclarationAstNode function) {
		try {
			align();
			out.write(function.getKind().name() + " ("
					+ function.getId().getName() + ")");
			println();
			indent();

			List<ParameterAstNode> parameters = function.getParameters();
			for (ParameterAstNode param : parameters) {
				param.accept(this);
			}

			BlockAstNode block = function.getBlock();
			block.accept(this);

			undent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitParameter(ParameterAstNode param) {
		try {
			align();
			out.write(param.getKind().name() + " (" + param.getName() + ")");
			println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitBlock(BlockAstNode block) {
		try {
			align();
			out.write(block.getKind().name());
			println();
			indent();
			List<AssignmentAstNode> variableDeclarations = block
					.getVariableDeclarations();
			for (AssignmentAstNode declr : variableDeclarations) {
				declr.accept(this);
			}
			block.getExpression().accept(this);
			undent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitOutput(OutputAstNode outputAstNode) {
		try {
			align();
			out.write(outputAstNode.getKind().name());
			println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
