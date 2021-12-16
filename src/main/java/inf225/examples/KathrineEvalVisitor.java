package inf225.examples;

import java.util.HashMap; 
import java.util.Map;
import inf225.grammars.*;

public class KathrineEvalVisitor extends KathrineGrammarBaseVisitor<Integer> {
	
	// minne for kalkulatoren 
	Map<String, Integer> memory = new HashMap<String, Integer>();
	
	 /** ID '=' expr NEWLINE */
	@Override
	public Integer visitAssign(KathrineGrammarParser.AssignContext ctx) {
		String id = ctx.ID().getText(); // id er venstreside av '='
		int value = visit(ctx.expr()); // regner ut verdien av høyresiden 
		memory.put(id, value); // lagre i minne
		return value;
	}
	
	 /** expr NEWLINE */
	@Override
	public Integer visitPrintExpr(KathrineGrammarParser.PrintExprContext ctx) {
		Integer value = visit(ctx.expr()); // evaluate the expr child 
		System.out.println(value); // print the result
		return 0; // return dummy value
	}
	
	/** INT */
	@Override
	public Integer visitInt(KathrineGrammarParser.IntContext ctx) {
		return Integer.valueOf(ctx.INT().getText()); 
	}
	
	/** ID */
	@Override
	public Integer visitId(KathrineGrammarParser.IdContext ctx) {
		String id = ctx.ID().getText();
		if ( memory.containsKey(id) ) 
			return memory.get(id); 
		return 0;
	}
	
	 /** expr op=('*'|'/') expr */
	@Override
	public Integer visitMulDiv(KathrineGrammarParser.MulDivContext ctx) {
		int left = visit(ctx.expr(0)); // get value of left subexpression 
		int right = visit(ctx.expr(1)); // get value of right subexpression 
		if ( ctx.op.getType() == KathrineGrammarParser.MUL ) 
			return left * right; 
		return left / right; // must be DIV
	}

	 /** expr op=('+'|'-') expr */
	@Override
	public Integer visitAddSub(KathrineGrammarParser.AddSubContext ctx) {
		int left = visit(ctx.expr(0)); // get value of left subexpression 
		int right = visit(ctx.expr(1)); // get value of right subexpression 
		if ( ctx.op.getType() == KathrineGrammarParser.ADD ) 
			return left + right; 
		return left - right; // must be SUB
	}
	
	 /** '(' expr ')' */
	@Override
	public Integer visitParens(KathrineGrammarParser.ParensContext ctx) {
		return visit(ctx.expr()); // return child expr's value 
	}
}