OBRACE: {
IDENT: int
IDENT: i1
EQUALS: =
INT: 7
SEMI: ;
IDENT: int
IDENT: i2
EQUALS: =
INT: 11
SEMI: ;
IF: if
OPARENT: (
IDENT: i2
EQEQ: ==
INT: 11
CPARENT: )
OBRACE: {
IDENT: i1
EQUALS: =
INT: 11
SEMI: ;
CBRACE: }
IDENT: elsif
OPARENT: (
IDENT: i2
EQEQ: ==
INT: 7
CPARENT: )
OBRACE: {
IDENT: i1
EQUALS: =
INT: 42
SEMI: ;
CBRACE: }
IF: if
OPARENT: (
IDENT: i1
EQEQ: ==
INT: 7
CPARENT: )
OBRACE: {
IDENT: i2
EQUALS: =
INT: 7
SEMI: ;
CBRACE: }
IDENT: elsif
OPARENT: (
IDENT: i1
EQEQ: ==
INT: 42
CPARENT: )
OBRACE: {
IDENT: i2
EQEQ: ==
INT: 42
SEMI: ;
CBRACE: }
CBRACE: }
