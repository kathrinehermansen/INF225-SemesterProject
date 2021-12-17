package inf225.examples;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.InputStream;
import inf225.grammars.*;

public class KathrineGrammar {
	
	public static void main (String[] args) throws Exception {
		
		String inputFile = "src/main/java/inf225/examples/grammarfile.txt";
		if ( args.length>0 ) inputFile = args[0];
		InputStream is = System.in;
		if ( inputFile!=null ) is = new FileInputStream(inputFile); 
		ANTLRInputStream input = new ANTLRInputStream(is);
		
		KathrineGrammarLexer lexer = new KathrineGrammarLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		KathrineGrammarParser parser = new KathrineGrammarParser(tokens);
		ParseTree tree = parser.program(); // parse; start at prog

		PrintParseTree prettyTree = new PrintParseTree();
		
		KathrineEvalVisitor eval = new KathrineEvalVisitor();
		System.out.println("The result is:");
		eval.visit(tree);
		System.out.println();
		System.out.println("Parse tree:");
		System.out.println();
		prettyTree.visit(tree);
		//System.out.println(tree.toStringTree(parser)); // print tree as text
	}

	
}