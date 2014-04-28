grammar Mu;

@header {
package com.cog.lang;
}

parse
 : block EOF
 ;

block
 : stat*
 ;

stat
 : assignment
 | if_stat
 | while_stat
 | log
 | exit_loop
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;

assignment
 : ID ASSIGN atom SCOL
 ;

if_stat
 : IF  condition_block (ELSE IF  condition_block )* (ELSE stat_block )?
 ;

condition_block
 : OPAR (not14Expr | logicalAtom)? CPAR stat_block
 ;

stat_block
 : stat_block_a
 | stat
 ;

stat_block_a
 : OBRACE block CBRACE
 ;

while_stat
 : WHILE OPAR (not14Expr | logicalAtom)? CPAR stat_block
 ;

log
 : LOG expr SCOL
 ;

exit_loop
 : EXIT_LOOP SCOL
 ;

//expr
// : functionCall
// | expr POW<assoc=right> expr #powExpr
// | MINUS expr         #unaryMinusExpr
// | expr MOD expr      #modExpr
// | expr MULT expr     #multExpr
// | expr DIV expr      #divExpr
// | expr PLUS expr     #plusExpr
// | expr MINUS expr    #minusExpr
// | expr LTEQ expr     #lteqExpr
// | expr GTEQ expr     #gteqExpr
// | expr LT expr       #ltExpr
// | expr GT expr       #gtExpr
// | expr NEQ expr      #neqExpr
// | expr EQ expr       #eqExpr
// | expr AND expr      #andExpr
// | expr OR expr       #orExpr
// | atom               #atomExpr
// ;

expr
 : unary14Expr
 | not14Expr
 | stringExpr
 ;

unary14Expr
 : MINUS mult13Expr
 | mult13Expr
 ;
 
mult13Expr
 : add12Expr ((MULT | DIV | MOD)? add12Expr)*
 ;
 
add12Expr
 : numericAtom ((PLUS | MINUS)? add12Expr)*
 ;

 
not14Expr
 : (NOT)? (eq9Expr | gt10Expr)+
 ;

gt10Expr
 : num_eq9Expr ((GT | LT | GTEQ | LTEQ)? num_eq9Expr)?
 ;
 
eq9Expr
	: or6Expr (EQ or6Expr)*
	;
 
num_eq9Expr
	: mult13Expr (EQ mult13Expr)*
	;
 
or6Expr
    :   and5Expr (OR and5Expr)*
    ;

and5Expr
    : logicalAtom (AND and5Expr)*
    ;
 
functionCall
 : ID OPAR (arg (COMMA arg)*)? CPAR 
 ;

arg
 : atom
 ;
 
stringExpr
 : STRING (PLUS atom)*
 ;
  
numericAtom
 : (INT | FLOAT) 
 | ID
 | OPAR unary14Expr CPAR
 | functionCall
 ;
 
logicalAtom
 : (TRUE | FALSE)
 | ID
 | OPAR not14Expr CPAR
// | functionCall
 ;
 
atom
 : numericAtom
 | logicalAtom
 | STRING         
 | NIL            
 ;

OR : '||';
AND : '&&';
EQ : '==';
NEQ : '!=';
GT : '>';
LT : '<';
GTEQ : '>=';
LTEQ : '<=';
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
MOD : '%';
POW : '^';
NOT : '!';

SCOL : ';';
ASSIGN : '=';
OPAR : '(';
CPAR : ')';
OBRACE : '{';
CBRACE : '}';
COMMA  : ',';

TRUE : 'true';
FALSE : 'false';
NIL : 'null';
IF : 'if';
ELSE : 'else';
WHILE : 'while';
LOG : 'log';
EXIT_LOOP : 'exit_loop';

ID
 : [a-zA-Z_][a-zA-Z_0-9]*
 ;

INT
 : [0-9]+
 ;

FLOAT
 : [0-9]+ '.' [0-9]* 
 | '.' [0-9]+
 ;

STRING
 : '"' (~["\r\n] | '""')* '"'
 ;

COMMENT
 : '#' ~[\r\n]* -> skip
 ;

SPACE
 : [ \t\r\n] -> skip
 ;

OTHER
 : . 
 ;