grammar SimpleLang;

prog: block;

block: (statement? NEWLINE)*;

statement: declaration
        | call_function
        | assignment;

// CONDITIONS

condition: ID if_operation comparable_value;

if_operation: EQUALS #eq
        | NOTEQUALS #neq
        | LESS #ls
        | GREATER #gr
        | LESSTHAN #lst
        | GREATERTHAN #grt
        ;

// VARIABLES AND OPERATIONS

type: INT_TYPE | REAL_TYPE | BOOL_TYPE;

declaration: type ID;

assignment: declaration '=' operation
            | ID '=' operation;

operation: expr0    #expression;

expr0:  expr1            #single0
      | expr0 '+' expr0       #add
;

expr1:  expr2            #single1
      | expr1 '-' expr1  #sub
;

expr2:  expr3            #single2
      | expr2 '*' expr2    #mul
;

expr3:  expr4            #single3
      | expr3 '/' expr3       #div
;

expr4:   expr5            #single4
      | expr4 AND expr4       #and
        | expr4 OR expr4        #or
        | expr4 XOR expr4       #xor
        | NOT expr4         #not
;

expr5:   INT            #int
       | REAL            #real
       | BOOL            #bool
       | ID              #id
       | condition       #conditionValue
       | '(' expr0 ')'        #par
;

//FUNCTIONS

call_function: function_name '(' arguments ')';

function_name: defined_functions
;

defined_functions: READ | PRINT;

arguments: value ',' arguments
        | value;

value: ID | INT | REAL | BOOL;

comparable_value: ID | INT | REAL | BOOL;

//TERMINALS

IF: 'if';
ENDIF: 'endif';
ELSE: 'else';
ENDELSE: 'endelse';
BEGIN: 'begin';
EQUALS: '==';
NOTEQUALS: '!=';
GREATER: '>';
LESS: '<';
LESSTHAN: '<=';
GREATERTHAN: '>=';

AND: '&&';
OR: '||';
NOT: '!';
XOR: 'xor';

READ : 'read';

PRINT : 'print';

INT_TYPE : 'int';

REAL_TYPE : 'real';

BOOL_TYPE : 'bool';

BOOL : 'true' | 'false';

ID : ('a'..'z'|'A'..'Z')+;

INT : '0'..'9'+;

REAL : '0'..'9'+'.''0'..'9'+;

NEWLINE: '\r'? '\n';

WS:   (' '|'\t')+ { skip(); }
    ;
