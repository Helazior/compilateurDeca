package fr.ensimag.deca.tree;

import org.junit.jupiter.api.Test;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.tools.SymbolTable;
/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class TestAssign {

    public static String gencodeSource(AbstractProgram source, DecacCompiler compiler) {
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }

    @Test
    public static void test1() {
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        SymbolTable symTab = new SymbolTable();
        ListDeclVar vars = new ListDeclVar();
        ListInst insts = new ListInst();
        vars.add(new DeclVar(
            new Identifier(symTab.create("int")),
            new Identifier(symTab.create("a")),
            new Initialization(new IntLiteral(3))
        ));
        vars.add(new DeclVar(
            new Identifier(symTab.create("int")),
            new Identifier(symTab.create("b")),
            new Initialization(new IntLiteral(10))
        ));
        vars.add(new DeclVar(
            new Identifier(symTab.create("float")),
            new Identifier(symTab.create("c")),
            new Initialization(new FloatLiteral(5))
        ));
        //insts.add(new Assign(new Identifier(symTab.create("a")), new IntLiteral(4)));
        //insts.add(new Assign(new Identifier(symTab.create("c")), new FloatLiteral(4.3f)));

        ListExpr exprs1 = new ListExpr();
        ListExpr exprs2 = new ListExpr();
        ListExpr exprs3 = new ListExpr();
        Identifier identifier1 = new Identifier(symTab.create("a"));
        identifier1.setType(compiler.intType());
        Identifier identifier2 = new Identifier(symTab.create("b"));
        identifier2.setType(compiler.intType());
        Identifier identifier3 = new Identifier(symTab.create("c"));
        identifier3.setType(compiler.floatType());
        exprs1.add(identifier1);
        exprs2.add(identifier2);
        exprs3.add(identifier3);
        insts.add(new Println(false, exprs1));
        insts.add(new Println(false, exprs2));
        insts.add(new Println(false, exprs3));
        AbstractMain main = new Main(vars, insts);
        AbstractProgram source = new Program(new ListDeclClass(), main);
        System.out.println(gencodeSource(source, compiler));
    }



    public static void main(String args[]) {
        test1();
    }
}
