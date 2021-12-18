package inf225.examples;

import java.util.HashMap;
import java.util.Map;
import inf225.grammars.*;
import org.antlr.v4.runtime.RuleContext;

import static java.lang.String.format;

public class PrintParseTree extends KathrineGrammarBaseVisitor<String> {

    private int sumOfChild;

    public PrintParseTree() {}

    public PrintParseTree(int sumOfChild) {
        this.sumOfChild = sumOfChild;
    }

    public int getSumOfChild() {
        return sumOfChild;
    }

    public void setSumOfChild(int sumOfChild) {
        this.sumOfChild = sumOfChild;
    }

    /** expr NEWLINE */
    @Override
    public String visitPrintExpr(KathrineGrammarParser.PrintExprContext ctx) {
        addSpacing();
        System.out.println("<expr>");

        increaseSpacing();
        visit(ctx.expr()); // evaluate the expr child
        decreaseSpacing();

        addSpacing();
        System.out.println("</expr>");
        return ""; // return dummy value
    }

    /** INT */
    @Override
    public String visitInt(KathrineGrammarParser.IntContext ctx) {
        addSpacing();
        System.out.println("<int>" + ctx.INT().getText() + "</int>");
        return "";
    }

    /** ID */
    @Override
    public String visitId(KathrineGrammarParser.IdContext ctx) {
        String id = ctx.ID().getText();
        System.out.println("id Visit");
        return id;
    }

    /** expr op=('*'|'/') expr */
    @Override
    public String visitMulDiv(KathrineGrammarParser.MulDivContext ctx) {
        if ( ctx.op.getType() == KathrineGrammarParser.MUL ) {
            div(ctx, "<mul>", "</mul>");
        } else {
            div(ctx, "<div>", "</div>");
        }
        return "";
    }

    /** expr op=('+'|'-') expr */
    @Override
    public String visitAddSub(KathrineGrammarParser.AddSubContext ctx) {
        if ( ctx.op.getType() == KathrineGrammarParser.ADD ) {
            add(ctx, "<add>", "</add>");
        } else {
            add(ctx, "<sub>", "</sub>");
        }
        return "";
    }

    public String add(KathrineGrammarParser.AddSubContext ctx, String addSub, String addSubDone) {
        addSpacing();
        System.out.println(addSub);

        increaseSpacing();
        String left = visit(ctx.expr(0)); // get value of left subexpression
        String right = visit(ctx.expr(1));
        decreaseSpacing();

        addSpacing();
        System.out.println(addSubDone);

        return "";
    }

    public String div(KathrineGrammarParser.MulDivContext ctx, String mulDiv, String mulDivDone) {
        addSpacing();
        System.out.println(mulDiv);

        increaseSpacing();
        String left = visit(ctx.expr(0)); // get value of left subexpression
        String right = visit(ctx.expr(1));
        decreaseSpacing();

        addSpacing();
        System.out.println(mulDivDone);

        return "";
    }

    @Override
    public String visitProgram(KathrineGrammarParser.ProgramContext ctx) {
        setSumOfChild(1);
        System.out.println("<program>");
        visitChildren(ctx);
        System.out.println("</program>");
        return "";
    }

    @Override
    public String visitAssign(KathrineGrammarParser.AssignContext ctx) {

        addSpacing();
        System.out.println("<stat>");

        increaseSpacing();
        visit(ctx.expr());
        decreaseSpacing();

        addSpacing();
        System.out.println("</stat>");
        return "";
    }

    @Override
    public String visitParens(KathrineGrammarParser.ParensContext ctx) {

        addSpacing();
        System.out.print("<parentheses>");
        System.out.println();

        increaseSpacing();
        visit(ctx.expr());
        decreaseSpacing();

        addSpacing();
        System.out.print("</parentheses>");
        System.out.println();
        return ""; // return child expr's value
    }

    public String addSpacing() {
        int formatFive = 3*getSumOfChild();
        int i = 0;

        while (i <= formatFive) {
            System.out.print(" ");
            i++;
        }
        return "";
    }

    public void increaseSpacing() {
        setSumOfChild(getSumOfChild() + 1);
    }

    public void decreaseSpacing() {
        setSumOfChild(getSumOfChild() - 1);
    }

}