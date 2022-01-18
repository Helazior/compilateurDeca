parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
    import fr.ensimag.deca.DecacCompiler;
    import fr.ensimag.deca.tools.SymbolTable.Symbol;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
}

prog returns[AbstractProgram tree]
    :  list_imports list_classes main EOF {
            //assert($list_classes.tree != null);
            assert($main.tree != null);

            if(getDecacCompiler().getCompilerOptions().getLinked()){
                $tree = new Program($list_imports.tree, $list_classes.tree, $main.tree);
            } else {
                $tree = new Program($list_classes.tree, $main.tree);
            }

            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type list_decl_var[$l,$type.tree] SEMI
    ;


list_decl_var[ListDeclVar l, AbstractIdentifier t]
    : dv1=decl_var[$t] {
        assert($dv1.tree != null);
        $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
            assert($dv2.tree != null);
            $l.add($dv2.tree);
        }
      )*
    ;

//TODO
decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
@init   {
            AbstractIdentifier name;
            AbstractInitialization initialization = new NoInitialization();
        }
    : i=ident {
            assert($i.tree != null);
            name = $i.tree;
        }
      (EQUALS e=expr {
            assert($e.tree != null);
            initialization = new Initialization($e.tree);
            setLocation(initialization, $e.start);
        }
      )? {
            $tree = new DeclVar($t, name, initialization);
            setLocation($tree, $i.start);
        }
    ;

// DONE
list_inst returns[ListInst tree]
@init {
    $tree = new ListInst();
}
    : (inst {
            assert($inst.tree != null);
            $tree.add($inst.tree);
        }
      )*
    ;

// DONE
inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree = $e1.tree;
        }
    | SEMI {
            $tree = new NoOperation();
            setLocation($tree, $SEMI);
        }
    | PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(false, $list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
    | PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(false, $list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
    | PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true, $list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
    | PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true, $list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
        }
    | WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE {
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($condition.tree, $body.tree);
            setLocation($tree, $condition.start);
        }
    //TODO
    | RETURN expr SEMI {
            assert($expr.tree != null);
            $tree = new Return($expr.tree);
            setLocation($tree, $expr.start);
        }
    ;

if_then_else returns[IfThenElse tree]
@init {
    // Pour pouvoir construire l'arbre dans le bon ordre, on liste les conditions
    // et instructions, puis on construit l'arbre depuis la fin
    List<AbstractExpr> conditions = new ArrayList<AbstractExpr>();
    List<Token> treeNodes = new ArrayList<Token>();
    List<ListInst> thens = new ArrayList<ListInst>();
    ListInst elseListInst = new ListInst(); // Si le else n'est pas spécifié, il est vide
}
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
        assert($condition.tree != null);
        assert($li_if.tree != null);

        conditions.add($condition.tree);
        thens.add($li_if.tree);

        treeNodes.add($condition.start);
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
            assert($elsif_cond.tree != null);
            assert($elsif_li.tree != null);

            conditions.add($elsif_cond.tree);
            thens.add($elsif_li.tree);

            treeNodes.add($elsif_cond.start);
        }
      )*
      (ELSE OBRACE li_else=list_inst CBRACE {
            assert($li_else.tree != null);
            elseListInst = $li_else.tree;
        }
      )?
      {
        // On est sûrs d'avoir au moins 1 élément
        AbstractExpr cond = conditions.remove(conditions.size() - 1);
        ListInst then = thens.remove(thens.size() - 1);
        Token node = treeNodes.remove(treeNodes.size() -1);

        $tree = new IfThenElse(cond, then, elseListInst);
        setLocation($tree, node);

        // On s'occupe du reste
        while (!conditions.isEmpty()) {
            assert(!thens.isEmpty());
            assert(!treeNodes.isEmpty());

            ListInst subtree = new ListInst();
            subtree.add($tree);

            cond = conditions.remove(conditions.size() - 1);
            then = thens.remove(thens.size() - 1);
            node = treeNodes.remove(treeNodes.size() -1);

            $tree = new IfThenElse(cond, then, subtree);
            setLocation($tree, node);
        }
        assert(thens.isEmpty());
        assert(treeNodes.isEmpty());
      }
    ;

list_expr returns[ListExpr tree]
@init {
    $tree = new ListExpr();
}
    : (e1=expr {
            assert($e1.tree != null);
            $tree.add($e1.tree);
        }
       (COMMA e2=expr {
            assert($e2.tree != null);
            $tree.add($e2.tree);
        }
       )* )?
    ;


expr returns[AbstractExpr tree]
    : assign_expr {
            assert($assign_expr.tree != null);
            $tree = $assign_expr.tree;
            setLocation($tree, $assign_expr.start);
        }
    ;


assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }

        }
        EQUALS e2=assign_expr {
            assert($e.tree != null);
            assert($e2.tree != null);
            $tree = new Assign((AbstractLValue)$e.tree, $e2.tree);
            setLocation($tree, $e.start);
        }
      | /* epsilon */ {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
      )
    ;

or_expr returns[AbstractExpr tree]
    : e=and_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=or_expr OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Or($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
       }
    ;

and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    |  e1=and_expr AND e2=eq_neq_expr {
            assert($e1.tree != null);                         
            assert($e2.tree != null);
            $tree = new And($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=eq_neq_expr EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=eq_neq_expr NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

// MODIFIED (instanceof not done)
inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=inequality_expr LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
        }
    ;

// DONE
sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=sum_expr PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Plus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=sum_expr MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Minus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

// DONE
mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=mult_expr TIMES e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Multiply($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=mult_expr SLASH e2=unary_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Divide($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=mult_expr PERCENT e2=unary_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Modulo($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

// DONE
unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree = new UnaryMinus($e.tree);
            setLocation($tree, $e.start);
        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree = new Not($e.tree);
            setLocation($tree, $e.start);
        }
    | select_expr {
            assert($select_expr.tree != null);
            $tree = $select_expr.tree;
            setLocation($tree, $select_expr.start);
        }
    ;
    
// MODIFIED
select_expr returns[AbstractExpr tree]
    : e=primary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
            
        }
        (o=OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            $tree = new MethodCall($e1.tree, $i.tree, $args.tree);
            setLocation($tree, $e1.start);
            
        }
        | /* epsilon */ {
            //Attention, ici le setLocation n'a pas ete fait pas defaut de token de depart!
            $tree = new Selection($e1.tree, $i.tree);
        }
        )
    ;

// MODIFIED
primary_expr returns[AbstractExpr tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
            This t = new This(true);
            $tree = new MethodCall(t, $m.tree, $args.tree);
            setLocation($tree, $m.start);
        }
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree = $expr.tree;
            setLocation($tree, $expr.start);
        }
    | READINT OPARENT CPARENT {
            $tree = new ReadInt();
        }
    | READFLOAT OPARENT CPARENT {
            $tree = new ReadFloat();
        }
    | NEW ident OPARENT CPARENT {
            assert($ident.tree != null);
            $tree= new New($ident.tree);
        }
    
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
        }
    | literal {
            assert($literal.tree != null);
            $tree = $literal.tree;
        }
    ;


type returns[AbstractIdentifier tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    ;

// MODIFIED
literal returns[AbstractExpr tree]
    : INT {
        assert($INT.text != null);
        String value = $INT.text;
        int valint = Integer.parseInt(value);
        $tree = new IntLiteral(valint);
        setLocation($tree, $INT);
        }
    | FLOAT {
        assert($FLOAT.text != null);
        String value = $FLOAT.text;
        float valfloat = Float.parseFloat(value);
        $tree = new FloatLiteral(valfloat);
        setLocation($tree, $FLOAT);
        }
    | STRING {
        assert($STRING.text != null);
        /* On enlève les guillemets */
        $tree = new StringLiteral($STRING.text);
        setLocation($tree, $STRING);
        }
    | TRUE {
        $tree = new BooleanLiteral(true);
        setLocation($tree, $TRUE);
        }
    | FALSE {
        $tree = new BooleanLiteral(false);
        setLocation($tree, $FALSE);
        }
    //TODO
    | THIS {
        $tree = new This(false);
        setLocation($tree, $THIS);
        }
    //TODO
    | NULL {
        $tree = new Null();
        setLocation($tree, $NULL);
        }
    ;

ident returns[AbstractIdentifier tree]
    : IDENT {
            DecacCompiler compiler = getDecacCompiler();
            $tree = new Identifier(compiler.createSymbol($IDENT.text));
            setLocation($tree, $IDENT);
        }
    ;

/****     Class related rules     ****/

list_classes returns[ListDeclClass tree]
@init {
    $tree = new ListDeclClass();
    }
    :
      (c1=class_decl {
          assert($c1.tree != null);
          $tree.add($c1.tree);
        }
      )*
    ;

class_decl returns [AbstractDeclClass tree]
    : CLASS name=ident superclass=class_extension OBRACE class_body CBRACE {
            assert($name.tree != null);
            assert($superclass.tree != null);
            assert($class_body.fields != null);
            assert($class_body.methodes != null);
            $tree = new DeclClass($name.tree, $superclass.tree, $class_body.fields, $class_body.methodes);
            setLocation($tree, $name.start);
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    | /* epsilon */ {
        //Attention, ici le setLocation n'a pas ete fait pas defaut de token de depart!
            $tree = new Identifier(getDecacCompiler().createSymbol("Object"));
        }
    ;

class_body returns[ListDeclMethod methodes, ListDeclField fields]
@init {
    $methodes = new ListDeclMethod();
    $fields = new ListDeclField();
}
    : (m=decl_method {
            assert($m.tree != null);
            $methodes.add($m.tree);
        }
      |n=decl_field_set[$fields] {
        }
      )* {
//Aurais-je oublie quelque chose?
        }
    ;

decl_field_set [ListDeclField f]
    : v=visibility t=type list_decl_field[$f, $t.tree, $v.tree] SEMI
    ;

visibility returns[Visibility tree]
    : /* epsilon */ { 
        $tree = Visibility.PUBLIC;
        }
    | PROTECTED {
        $tree = Visibility.PROTECTED;
        }
    ;

list_decl_field [ListDeclField f, AbstractIdentifier t, Visibility v] 
    : dv1=decl_field[$t, $v]{
        assert($dv1.tree != null);
        $f.add($dv1.tree);
        }
        (COMMA dv2=decl_field[$t, $v]{
            assert($dv2.tree != null);
            $f.add($dv2.tree);
        }
      )*
    ;

decl_field [AbstractIdentifier t, Visibility v] returns[AbstractDeclField tree]
@init   {
            AbstractIdentifier field;
            AbstractInitialization initialization = new NoInitialization();
        }
    : i=ident {
            assert($i.tree != null);
            field = $i.tree;
        }
      (EQUALS e=expr {
            assert($e.tree != null);
            initialization = new Initialization($e.tree);
          
        }
      )? {
            $tree = new DeclField(t, field, initialization, v);
            setLocation($tree, $i.start);
        }
    ;

decl_method returns[DeclMethod tree]
    : type ident OPARENT params=list_params CPARENT (block {
            assert($block.decls != null);
            assert($block.insts != null);
            MethodBody methodBody = new MethodBody($block.decls, $block.insts);
            setLocation(methodBody, $block.start);
            $tree = new DeclMethod($type.tree, $ident.tree, $params.tree, methodBody);

        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
            assert($code.text != null);
            assert($code.location != null);
            MethodAsmBody methodBody = new MethodAsmBody($code.text, $code.location);
            setLocation(methodBody, $multi_line_string.start);
            $tree = new DeclMethod($type.tree, $ident.tree, $params.tree, methodBody);

        }
      ) {
            assert($type.tree != null);
            assert($ident.tree != null);
            assert($params.tree != null);
            setLocation($tree, $type.start);
        }
    ;

list_params returns[ListDeclParam tree]
@init {
    $tree = new ListDeclParam();
}
    : (p1=param {
        assert($p1.tree != null);
        $tree.add($p1.tree);
        } (COMMA p2=param {
            assert($p2.tree != null);
            $tree.add($p2.tree);
        }
      )*)?
    ;
    
multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param returns[DeclParam tree]
    : type ident { 
        assert($type.tree != null);
        assert($ident.tree != null);
        $tree = new DeclParam($type.tree, $ident.tree);
        setLocation($tree, $type.start);
        }
    ;

/****     Imports related rules     ****/

list_imports returns[ListDeclImport tree]
@init{
    $tree = new ListDeclImport();
}
    : (IMPORT import_decl EOL{
        assert($import_decl.tree != null);
        $tree.add($import_decl.tree);
    }
    )*
    ;

import_decl returns[AbstractProgram tree]
    : FILENAME {
        //$tree = getDecacCompiler().compileImport($FILENAME.text);
        //assert($tree != null);
        //setLocation($tree, $FILENAME);
    }
    ;
