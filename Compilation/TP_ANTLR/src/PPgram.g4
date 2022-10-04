grammar PPgram;
// $Id$

//types
type : 'integer'
    | 'boolean'
    | 'array of' type ;

//constantes
const : 'true'
    |'false'
    | Number;

//opérateurs unaires
uop : '-'
    | 'not';

//opérateurs Binaires logiques
logbop : 'and'
    | 'or'
    | '<'
    | '<='
    | '='
    | '!='
    | '>='
    | '>' ;

bop1 : ('*'|'/');

bop2 : ('+'|'-');

//cibles d'appel
call : 'read'
    | 'write'
    | ID ;


//expressions
exp : const
    | ID
    | uop exp
    | exp bop1 exp
    | exp bop2 exp
    | exp logbop exp
    | call'('exp*')'
    | exp'['exp']'
    | 'new''array''of' type '['exp']'
    | '('exp')';

//instructions
inst : ID ':=' exp
    | exp'['exp']' ':' '=' exp
    | 'if' exp 'then' inst 'else' inst
    | 'while' exp 'do' inst
    | call'('exp*')'
    | 'skip'
    | inst';'inst ;

//fonctions/procedures
funcproc : ID'('(ID':'type)* ')' (':' type)?
    ('var'(ID':'type)+)?
    inst;

//programmes
prog :  ('var'(ID':'type)+)?
    funcproc*
    inst;





//REGLES LEXICALES

// match lower-case identifiers
ID : [a-zA-Z]+ ;

// A number is an integer value
Number : ('0'..'9')+ ;

// We're going to ignore all white space characters
WS : [ \t\r\n]+ -> skip ;
