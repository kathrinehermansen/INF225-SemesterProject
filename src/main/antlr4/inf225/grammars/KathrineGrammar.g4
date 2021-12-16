grammar KathrineGrammar;

program : stat+ 				; 					// begin parsing here

stat 	: expr NEWLINE				#printExpr
		| ID '=' expr NEWLINE		#assign
		| NEWLINE					#blank
		;

expr	: expr op=('*'|'/') expr 	#MulDiv			// match expression with + operator
		| expr op=('+'|'-') expr 	#AddSub			// match expression with * operator
		| INT 						#int			// simple atomic int
		| ID						#id
		| '(' expr ')'				#parens
		;
		
INT		: [0-9]+ 				;					// match integers
ID		: [a-zA-Z]+ 			;					// match identifiers
NEWLINE	: '\r'? '\n' 			;					// return newline to parser 
WS 		: [ \t]+ -> skip  		;					// toss out whitespace

MUL		: '*'					;
DIV		: '/'					;
ADD		: '+'					;
SUB 	: '-'					;