# [ANTLR4](https://www.antlr.org/) Example Project

### Links
* [ANTLR4 Documentation](https://github.com/antlr/antlr4/blob/master/doc/index.md)
* [ANTLR4 Getting Started](https://github.com/antlr/antlr4/blob/master/doc/getting-started.md)
* [Java parsing with ANTLR](https://www.baeldung.com/java-antlr) tutorial from [Baeldung](https://www.baeldung.com/)

# Examples
## Hello
The grammar `src/main/antlr4/inf225/grammars/Hello.g4` defines a tiny *Hello, World!* language:

```antlr4
// Define a grammar called Hello
grammar Hello;
hello : 'hello' ID '!';   // non-terminal hello: match keyword hello followed by an identifier
ID : [a-z]+ ;             // terminal ID: match lower-case identifiers
WS : [ \t\r\n]+ -> skip ; // terimanl WS: skip spaces, tabs, newlines
```

A *Hello* ‘program‘ is defined by the `hello` production, and consists of the string `hello`, followed by an identifier and a `!`. E.g., valid (and boring) hello texts would be `"hello world!"`, `"hello\nworld!"` or `"  hello you !"`. Invalid examples would be `"hello world"` (missing `!`), `"Hello World!"` (uppercase `Hello`) or `"hello!"` (missing identifier). Note that ANTLR4 by default makes literal terminals (e.g., `'hello'` above) *reserved keywords*, so `"hello hello!"` is not valid (`hello` will never match `ID`).

When the project is built (during the `generate-sources` phase), ANTLR4 will generate several Java classes implementing a parser for the *Hello* language:

* `HelloLexer.java` – the *lexer*, which splits an input string/stream into a stream of *tokens* or words (`'hello'`, `ID` or `WS` for this grammar; whitespace (`WS`) is ignored)
* `HelloParser.java` – the *parser*, which recognizes the sentence structure of the input text, and (optionally) builds a *parse tree*
* `HelloListener.java` – a *listener* interface, used together with a parse tree walker to perform an action for each node in the parse tree
* `HelloBaseListener.java` – a *listener* class, with default do-nothing methods for each type of parse tree node 

We can use the parser like this (see example in `src/main/java/inf225/examples/HelloExample.java`):

* First, set up the input and the lexer; this will give us a stream of tokens (words):

```java
String input = "hello world";
// a lexer that splits the input string into tokens
HelloLexer lexer = new HelloLexer(CharStreams.fromString(input));
// a stream of tokens to feed to the parser
CommonTokenStream tokens = new CommonTokenStream(lexer);
```

* Next, make a `HelloParser` that reads the tokens:

```java
HelloParser parser = new HelloParser(tokens);
```

* Finally, we can get the *context* for the non-terminal we're interested in – the parser will then try to match the input to the production rule for the non-terminal (`hello: 'hello' ID '!'` in our case – i.e., we expect to find a `hello` token, an identifier and an exclamation mark):

```java
// the method name here matches the name of the non-terminal in the grammar (hello)
HelloContext tree = parser.hello();
```
### Tokens
You can easily examine the token stream by asking for a list of tokens. First, you must make sure that all the input has been processed, by calling `fill()`. Tthe parser will normally read tokens one by one (possibly looking ahead a few tokens), so the lexer produces tokens on demand – `fill()` makes it finish the job.

```java
// process all the input
tokens.fill();
		
// look at each token
for (Token t : tokens.getTokens())
	System.out.printf("%-10s (%s)%n", t.getText(), HelloLexer.VOCABULARY.getDisplayName(t.getType()));
```

Given the input `hello world!`, the output should look like this:

```
hello      ('hello')
world      (ID)
!          ('!')
<EOF>      (EOF)
```

Each `Token` contains information about its type, it source (e.g., filename), start/end offset, and line number and column of the first character:

```java
for (Token t : tokens.getTokens())
	System.out.printf("%-10s #%d, offset=%2d–%2d, line=%d, column=%2d, source=%s%n", t.getText(), t.getTokenIndex(),
			t.getStartIndex(), t.getStopIndex(), t.getLine(), t.getCharPositionInLine(), t.getTokenSource().getSourceName());
```
E.g.:

```
hello      #0, offset= 0– 4, line=1, column= 0, source=<unknown>
world      #1, offset= 6–10, line=1, column= 6, source=<unknown>
!          #2, offset=11–11, line=1, column=11, source=<unknown>
<EOF>      #3, offset=12–11, line=1, column=12, source=<unknown>
```

The token stream itself gives you enough information to do very simple syntax highlighting; e.g., adding colours for keywords, string literals and so on.

### Parse trees
To see the parse result, we can use a `ParseTreeWalker` to visit all the nodes in the parse tree, giving it a listener that will be called for each node:

```java
walker.walk(new HelloBaseListener() {
	@Override
	public void visitTerminal(TerminalNode node) {
		System.out.println("'" + node + "' ");
	}
	// you can also add visit methods for error nodes, and before and after a non-terminal
}, tree);
```

The output should look like this (for input `hello world!`):

```
'hello' 
'world' 
'!' 
```

A more interesting walker would pick out who we're saying hello to:

```java
new ParseTreeWalker().walk(new HelloBaseListener() {
	@Override
	public void enterHello(HelloContext ctx) {
		System.out.print("Saying hello to '" + ctx.getChild(1) + "'!");
	}
}, tree);
```

Giving the output `Saying hello to 'world'!`

## Expresssions
For a more interesting example, have a look at `Expr.g4` and `ExprExample.java`, which defines a very simple language for prefix expressions with a single operator (`+`). The tree walker is used to evaluate the expressions using a stack: literal numbers are pushed onto the stack, and the plus operator pops to numbers, adds them and pushes the result. Try improving it by adding more operators!
 
		
# Maven Setup
This project comes with a working Maven `pom.xml` file. You should be able to import it into Eclipse using *File → Import → Maven → Existing Maven Projects* (or *Check out Maven Projects from SCM* to do Git cloning as well). You can also build the project from the command line with `mvn package`.

Pay attention to these folders:
* `src/main/java` – Java source files go here (as usual for Maven)
* `src/main/antlr4` – ANTLR4 grammar files (`*.g4`) go here; use sub-folders to place the generated parser in a specific Java package
* `src/test/java` – JUnit tests
* `target/generated-sources/antlr4` – ANTLR4 will place Java source code here (this happens automatically during compilation or if you run `mvn generate-sources`)
* `target/classes` – compiled Java class files
* `target/*.jar` – your compiled project, packaged in a JAR file

#### POM snippets
If you're setting up / adding ANTLR4 to your own project, you can cut and paste these lines into your `pom.xml`file.

* You should make sure that both the parser generator and the runtime use the same version, so define the version number in `<properties>…</properties>`:

```xml
		<antlr4.version>4.8-1</antlr4.version>
```

* The ANTLR4 runtime is needed to run the compiled parser; add it in the `<depencencies>…</dependencies>` section:

```xml
<!-- https://mvnrepository.com/artifact/org.antlr/antlr4-runtime -->
<dependency>
	<groupId>org.antlr</groupId>
	<artifactId>antlr4-runtime</artifactId>
	<version>${antlr4.version}</version>
</dependency>
```

* The ANTLR4 maven plugin includes the ANTLR4 tool, and is needed to generate parser during compilation; add it to `<build><plugins>…</plugins></build>`:

```xml
<plugin>
	<groupId>org.antlr</groupId>
	<artifactId>antlr4-maven-plugin</artifactId>
	<version>${antlr4.version}</version>
	<executions>
		<execution>
			<goals>
				<goal>antlr4</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```

