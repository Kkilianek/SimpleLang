grammar SimpleLang;

// Plik zawiera definicję gramatyki prostego języka programowania - SimpleLang

program: statement*;

statement: assignment | input | output;

assignment: ID '=' expression ';'  #assignmentStatement;

input: 'read' ID ';'               # readStatement;

output: 'print' expression ';'     # printStatement;

expression: '(' expression ')'              # parensExpr
          | expression ('*' | '/') expression      # mulDivExpr
          | expression ('+' | '-') expression      # addSubExpr
          | ID                            # idExpr
          | INT                           # intExpr
          | FLOAT                         # floatExpr
          | STRING                        # stringExpr
          | '(' 'int' ')' expression       # intConversionExpr
          | '(' 'float' ')' expression     # floatConversionExpr;

ID: [a-zA-Z]+;
INT: [0-9]+;
FLOAT: [0-9]+'.'[0-9]+;
STRING: '"' .*? '"';

WS: [ \t\r\n]+ -> skip;
