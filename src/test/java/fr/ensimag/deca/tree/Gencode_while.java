/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class Gencode_while {

    public static AbstractProgram initTest1() {
        // TODO : quand on aura des variables^^
        SymbolTable st = new SymbolTable();
        ListInst linst = new ListInst();
        ListInst linsttrue = new ListInst();
        ListExpr exprtrue = new ListExpr();
        ListExpr exprfalse = new ListExpr();
        exprtrue.add(new StringLiteral("\"Dans le While !\""));
        linsttrue.add(new Println(false, exprtrue));
        Equals p1 = new Equals(new IntLiteral(50), new IntLiteral(50));     // true
        Equals p2 = new Equals(p1, new BooleanLiteral(true));                    // true
        While ite = new While(p2, linsttrue);
        linst.add(ite);
        AbstractProgram source =
                new Program(
                        new ListDeclClass(),
                        new Main(new ListDeclVar(),linst));
        return source;
    }

    public static String gencodeSource(AbstractProgram source) {
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }

    public static void test1() {
        AbstractProgram source = initTest1();
        //System.out.println("---- From the following Abstract Syntax Tree ----");
        //source.prettyPrint(System.out);
        //System.out.println("---- We generate the following assembly code ----");
        String result = gencodeSource(source);
        System.out.println(result);

    }



    public static void main(String args[]) {
        test1();
    }
}
