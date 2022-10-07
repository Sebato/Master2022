grammar PP;

type returns [Type value] :
        'integer'           { $value = new Int(); }
        | 'boolean'         { $value = new Bool(); }
        | 'array of' type   { $value = new Array($type.value);}
    ;

callee returns [Callee value] :
        'read'      {$value = new Read();}
        | 'write'   {$value = new Write();}
        | ID{$value = new User($ID.text);}
    ;

expr returns [PPExpr value]
    @init { ArrayList<PPExpr> exprList = new ArrayList<PPExpr>();} :

        BOOLEAN
        {
            if($BOOLEAN.text.equals("true")){$value = new PPTrue();}
            if($BOOLEAN.text.equals("false")){$value = new PPFalse();}
        }
        | INTEGER   { $value = new PPCte(Integer.valueOf($INTEGER.text));}
        | ID        { $value = new PPVar($ID.text); }

        | '('ex = expr')'   { $value = $ex.value; }

        | '-' ex = expr     { $value = new PPInv($ex.value); }
        | 'not' ex = expr   { $value = new PPNot($ex.value); }

        | ex1 = expr '+' ex2 = expr  { $value = new PPAdd($ex1.value, $ex2.value);}
        | ex1 = expr '-' ex2 = expr  { $value = new PPSub($ex1.value, $ex2.value);}
        | ex1 = expr '*' ex2 = expr  { $value = new PPMul($ex1.value, $ex2.value);}
        | ex1 = expr '/' ex2 = expr  { $value = new PPDiv($ex1.value, $ex2.value);}
        | ex1 = expr 'and' ex2 = expr{ $value = new PPAnd($ex1.value, $ex2.value);}
        | ex1 = expr 'or' ex2 = expr { $value = new PPOr($ex1.value, $ex2.value);}
        | ex1 = expr '<'  ex2 = expr { $value = new PPLt($ex1.value, $ex2.value);}
        | ex1 = expr '<=' ex2 = expr { $value = new PPLe($ex1.value, $ex2.value);}
        | ex1 = expr '='  ex2 = expr { $value = new PPEq($ex1.value, $ex2.value);}
        | ex1 = expr '!=' ex2 = expr { $value = new PPNe($ex1.value, $ex2.value);}
        | ex1 = expr '>=' ex2 = expr { $value = new PPGe($ex1.value, $ex2.value);}
        | ex1 = expr '>'  ex2 = expr { $value = new PPGt($ex1.value, $ex2.value);}

        | c = callee '('(expr {exprList.add($expr.value);})* ')'
        { $value = new PPFunCall($c.value, exprList); }

        | ex1 = expr'[' ex2 = expr ']' { $value = new PPArrayGet($ex1.value, $ex2.value);}
        | 'new' 'array' 'of' t = type '['ex = expr']' {$value = new PPArrayAlloc($t.value, $ex.value);}
    ;


inst returns [PPInst value]
    @init { ArrayList<PPExpr> exprList = new ArrayList<PPExpr>();} :
        //PPAssign
        ID ':=' ex = expr { $value = new PPAssign($ID.text, $ex.value);}

        //PPArraySet
        | arr = expr'['index = expr']' ':=' val = expr
        { $value = new PPArraySet($arr.value, $index.value, $val.value);}

        //PPCond
        | 'if' cond = expr 'then' i1 = inst 'else' i2 = inst
        { $value = new PPCond($cond.value, $i1.value, $i2.value);}

        //PPWhile
        | 'while' cond = expr 'do' i = inst
        { $value = new PPWhile($cond.value, $i.value);}

        //PPProcCall
        | c = callee'('(expr {exprList.add( $expr.value );})* ')'
        { $value = new PPProcCall ($c.value, exprList);}

        //PPSkip
        | 'skip' { $value = new PPSkip();}

        //PPSeq
        | i1 = inst ';' i2 = inst
        { $value = new PPSeq($i1.value, $i2.value);}
    ;

def returns [PPDef value] :
        fun {$value = $fun.value;}
        |proc {$value = $proc.value;}
    ;

fun returns [PPFun value]
    @init {
        ArrayList<Pair<String,Type>>  args = new   ArrayList<Pair<String,Type>>();
        ArrayList<Pair<String,Type>>  locals = new ArrayList<Pair<String,Type>>();
        } :
        //PPFun
        name = ID '('
        ( arg = ID ':' type {args.add(new Pair<String, Type>($arg.text, $type.value) );})*
        ')'':' ret = type
        ('var' ( ID ':' type {locals.add(new Pair<String, Type>($ID.text, $type.value) );})+)?
        code = inst
        { $value = new PPFun($name.text, args, locals, $code.value, $ret.value);}
    ;

proc returns [PPProc value]
    @init {
        ArrayList<Pair<String,Type>>  args = new   ArrayList<Pair<String,Type>>();
        ArrayList<Pair<String,Type>>  locals = new ArrayList<Pair<String,Type>>();
        } :
        //PPProc
        name = ID '('
        ( arg = ID ':' type {args.add(new Pair<String, Type>($arg.text, $type.value) );})*
        ')'
        ('var' ( ID ':' type {locals.add(new Pair<String, Type>($ID.text, $type.value) );})+)?
        code = inst
        { $value = new PPProc($name.text, args, locals, $code.value);}
    ;

prog returns [PPProg value]
    @init {
        ArrayList<Pair<String,Type>> globals = new ArrayList<Pair<String,Type>>();
        ArrayList<PPDef> defs = new ArrayList<PPDef>();
        } :
        //PPProg
        ('var' ( ID ':' type {globals.add(new Pair<String, Type>($ID.text, $type.value) );})+)?
        (def {defs.add($def.value);})*
        code = inst
        { $value = new PPProg(globals, defs, $code.value);}
    ;

BOOLEAN :   'true' | 'false' ;
INTEGER :   ('0'..'9')+ ;
ID      :   [A-Za-z]+   ;
WS      :   [ \t\r\n]+ -> skip ;