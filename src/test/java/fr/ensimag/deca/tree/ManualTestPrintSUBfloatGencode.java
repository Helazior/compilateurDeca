/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.CompilerOptions;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class ManualTestPrintSUBfloatGencode {

    public static AbstractProgram initTest1() {
        SymbolTable st = new SymbolTable();
        Type float_t = new FloatType(st.create("float"));
        ListInst linst = new ListInst();
        ListExpr exp1 = new ListExpr();
        linst.add(new Print(false,exp1));
        //(3.2 - 2.9) * 4 = 1.2
        Minus p1 = new Minus(new FloatLiteral(3.2f), new FloatLiteral(2.9f));
        Multiply p2 = new Multiply(p1, new FloatLiteral(4.0f));
        p1.setType(float_t);
        p2.setType(float_t);
        exp1.add(p2);
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
        // source.prettyPrint(System.out);
        //System.out.println("---- We generate the following assembly code ----");
        String result = gencodeSource(source);
        System.out.println(result);

    }



    public static void main(String args[]) throws ContextualError {
        test1();
    }
}
