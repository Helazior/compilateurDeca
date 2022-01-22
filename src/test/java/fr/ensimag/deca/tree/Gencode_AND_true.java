/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class Gencode_AND_true {

    public static AbstractProgram initTest1() {
        SymbolTable st = new SymbolTable();
        ListInst linst = new ListInst();
        ListInst linsttrue = new ListInst();
        ListInst linstfalse = new ListInst();
        ListExpr exprtrue = new ListExpr();
        ListExpr exprfalse = new ListExpr();
        exprtrue.add(new StringLiteral("\"true !\""));
        exprfalse.add(new StringLiteral("\"false !\""));
        linsttrue.add(new Print(false, exprtrue));
        linstfalse.add(new Print(false, exprfalse));
        And p1 = new And(
                new Equals(new IntLiteral(50), new IntLiteral(50)),         // true
                new BooleanLiteral(true));                                        // true
        And p2 = new And(p1, new BooleanLiteral(true));                     // true
        IfThenElse ite = new IfThenElse(p2, linsttrue, linstfalse);
        linst.add(ite);
        AbstractProgram source =
                new Program(
                        new ListDeclClass(),
                        new Main(new ListDeclVar(),linst));
        return source;

    }

    public static String gencodeSource(AbstractProgram source) throws ContextualError, DecacFatalError {
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }

    public static void test1() throws ContextualError, DecacFatalError {
        AbstractProgram source = initTest1();
        //System.out.println("---- From the following Abstract Syntax Tree ----");
        //source.prettyPrint(System.out);
        //System.out.println("---- We generate the following assembly code ----");
        String result = gencodeSource(source);
        System.out.println(result);

    }



    public static void main(String args[]) throws ContextualError, DecacFatalError {
        test1();
    }
}
