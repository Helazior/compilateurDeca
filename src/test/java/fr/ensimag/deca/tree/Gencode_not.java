/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class Gencode_not {

    public static AbstractProgram initTest1() {
        SymbolTable st = new SymbolTable();
        Type int_t = new IntType(st.create("int"));
        ListInst linst = new ListInst();
        ListInst linsttrue = new ListInst();
        ListInst linstfalse = new ListInst();
        ListExpr exprtrue = new ListExpr();
        ListExpr exprfalse = new ListExpr();
        exprtrue.add(new StringLiteral("\"True !\""));
        exprfalse.add(new StringLiteral("\"False !\""));
        linsttrue.add(new Print(false, exprtrue));
        linstfalse.add(new Print(false, exprfalse));
        Not p1 = new Not(new Equals(new IntLiteral(50), new IntLiteral(50)));     // false
        Equals p2 = new Equals(p1, new BooleanLiteral(false));                    // true
        p1.setType(int_t);
        p2.setType(int_t);
        IfThenElse ite = new IfThenElse(p2, linsttrue, linstfalse);
        linst.add(ite);
        AbstractProgram source =
                new Program(
                        new ListDeclClass(),
                        new Main(new ListDeclVar(),linst));
        return source;

    }

    public static String gencodeSource(AbstractProgram source) throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }

    public static void test1() throws ContextualError {
        AbstractProgram source = initTest1();
        //System.out.println("---- From the following Abstract Syntax Tree ----");
        //source.prettyPrint(System.out);
        //System.out.println("---- We generate the following assembly code ----");
        String result = gencodeSource(source);
        System.out.println(result);

    }



    public static void main(String args[]) throws ContextualError {
        test1();
    }
}
