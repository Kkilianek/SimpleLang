grammar SimpleLang;

prog: (statement? NEWLINE)*;

statement: declaration
        | call_function
        | assignment
        ;

declaration: type ID ;

type: 'int' | 'real';

assignment: declaration '=' operation
            | ID '=' operation;

operation: expr0 ;


expr0:  expr1            #single0
      | expr1 '+' expr0        #add
      | expr1 '-' expr0        #del
;

expr1:  expr2            #single1
      | expr2 '*' expr0    #mult
      | expr2 '/' expr0    #dif
;

expr2:   INT            #int
       | REAL            #real
       | ID              #id
       | '(' expr0 ')'        #par
;

call_function: ID '(' arguments ')';

arguments: value ',' arguments
        | value;

value: ID | INT | REAL;

NEWLINE: '\r'? '\n';

WS:   (' '|'\t')+ { skip(); }
    ;
ID : ('a'..'z'|'A'..'Z')+;

INT : '0'..'9'+;

REAL : '0'..'9'+'.''0'..'9'+;