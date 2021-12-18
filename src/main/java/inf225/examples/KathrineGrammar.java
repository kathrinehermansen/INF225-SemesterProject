package inf225.examples;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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

		String inputstring = readFileAsString(inputFile);

		System.out.println("Inserted math equations:");
		System.out.println(inputstring);
		System.out.println("The results are:");
		eval.visit(tree);
		System.out.println();
		System.out.println("Parse tree");
		System.out.println();
		prettyTree.visit(tree);
		//System.out.println(tree.toStringTree(parser)); // print tree as text
	}

	public static String readFileAsString(String fileName) {
		String text = "";
		try {
			text = new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;
	}
	
}