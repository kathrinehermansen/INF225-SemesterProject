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
		
		KathrineEvalVisitor eval = new KathrineEvalVisitor();
		eval.visit(tree);
		
		//System.out.println(tree.toStringTree(parser)); // print tree as text
	}
	
}