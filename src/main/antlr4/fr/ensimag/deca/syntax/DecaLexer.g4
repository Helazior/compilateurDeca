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
// Deca lexer rules.

//RESERVED WORDS
   ASM: 'asm';
   CLASS: 'class';
   EXTENDS: 'extends';
   ELSE: 'else';
   FALSE: 'false';
   IF: 'if';
   INSTANCEOF: 'instanceof';
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
//


SEMI: ';';
EOL: '\n' {skip(); };
CR: '\r' {skip(); };
TAB: '\t' {skip(); };
SPACE: ' ' {skip(); };
DOT: '.';
COMMA: ',';
EXCLAM: '!';
GUI: '\'';


OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';

EQUALS: '=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
ANTISLASH: '\\';
PERCENT: '%';

GT: '>';
LT: '<';
EQEQ: '==';
NEQ: '!=';
GEQ: '>=';
LEQ: '<=';
AND: '&&';
OR: '||';

//INDENT
   fragment DIGIT: ('0' .. '9');
   fragment LETTER: ('a' .. 'z' | 'A' .. 'Z');
   IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
//

//STRING
   fragment STRING_CAR: ~('"' | '\\' | '\n');
   STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
   MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';
//

//COMMENT
   COMMENT: '//' .*? EOL {skip(); };
   MULTI_LINE_COMMENT: '/*'.*? '*/' {skip(); };
//

//INT
   fragment POSITIVE_DIGIT: '1' .. '9';
   INT: ('0' | POSITIVE_DIGIT* DIGIT);
//

//FLOAT
   fragment NUM: DIGIT+;
   fragment SIGN: ('+' | '-')?;

   fragment EXP: ('E' | 'e') SIGN NUM;
   fragment DEC: NUM '.' NUM;
   fragment FLOATDEC: (DEC | DEC EXP) ('F' | 'f')?;

   fragment DIGITHEX: ('0' .. '9' | 'A' .. 'F' | 'a' .. 'f');
   fragment NUMHEX: DIGITHEX+;
   fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f')?;

   FLOAT: (FLOATDEC | FLOATHEX);
//

//INCLUDE
   FILENAME: '"' (LETTER | DIGIT | '.' | '-' | '_')+ '"';
   IMPORT: 'import';
   INCLUDE: '#include' (' ')* FILENAME;
//

//CLASS
