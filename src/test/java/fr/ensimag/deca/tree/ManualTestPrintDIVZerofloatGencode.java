/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class ManualTestPrintDIVZerofloatGencode {

    public static AbstractProgram initTest1() {
        SymbolTable st = new SymbolTable();
        Type float_t = new FloatType(st.create("float"));
        ListInst linst = new ListInst();
        ListExpr exp1 = new ListExpr();
        linst.add(new Print(false,exp1));
        // 39.2 / 2.9 / 4.0 =
        Divide p1 = new Divide(new FloatLiteral(39.2f), new FloatLiteral(2.9f));
        Divide p2 = new Divide(p1, new FloatLiteral(0.0f));
        p1.setType(float_t);
        p2.setType(float_t);
        exp1.add(p2);
        AbstractProgram source =
                new Program(
                        new ListDeclClass(),
                        new Main(new ListDeclVar(),linst));
        return source;
    }

    public static String gencodeSource(AbstractProgram source) {
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(),null);
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }

    public static void test1() {
        AbstractProgram source = initTest1();
        //System.out.println("---- From the following Abstract Syntax Tree ----");
        // source.prettyPrint(System.out);
        //System.out.println("---- We generate the following assembly code ----");
        String result = gencodeSource(source);
        System.out.println(result);

    }



    public static void main(String args[]) {
        test1();
    }
}
