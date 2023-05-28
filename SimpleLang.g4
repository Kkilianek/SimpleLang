grammar SimpleLang;

// Plik zawiera definicję gramatyki prostego języka programowania - SimpleLang

program: statement*;

statement: assignment | input | output;

assignment: ID '=' expression ';';

input: 'read' ID ';';

output: 'print' expression ';';

expression: '(' expression ')'              # parensExpr
          | expression ('*' | '/') expression      # mulDivExpr
          | expression ('+' | '-') expression      # addSubExpr
          | ID                            # idExpr
          | INT                           # intExpr
          | FLOAT                         # floatExpr
          | STRING                        # stringExpr;

ID: [a-zA-Z]+;
INT: [0-9]+;
FLOAT: [0-9]+'.'[0-9]+;
STRING: '"' .*? '"';

WS: [ \t\r\n]+ -> skip;
