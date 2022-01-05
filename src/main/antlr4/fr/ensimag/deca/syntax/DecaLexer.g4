lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}
ASM: 'asm';
//CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
EOL: '\n';
FALSE: 'false';
IF: 'if';
//INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTLNX: 'printlnx';
PRINTX: 'printx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
TRUE: 'true';
WHILE: 'while';

DIGIT: ('0' .. '9');
LETTER: ('a' .. 'z' | 'A' .. 'Z');
IDENT: (LETTER + '$' + '_')(LETTER + DIGIT + '$' + '_')*;

INF: '<';
SUP: '>';
ASS: '=';
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
MOD: '%';
DOT: '.';
COMMA: ',';
OPARENT: '(';
CPARENT: ')';
OCROC: '{';
CCROC: '}';
EX: '!';
DOTCOMMA: ';';
EQ: '==';
NEQ: '!=';
SUPEQ: '>=';
INFEQ: '<=';
AND: '&&';
OR: '||';

STRING_CAR: ~('/' | '"' | '\n');
STRING: '"' (STRING_CAR + '\\"' + '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR + '\\"' + '\\\\' + EOL)* '"';

DUMMY_TOKEN: .;
// Deca lexer rules.
