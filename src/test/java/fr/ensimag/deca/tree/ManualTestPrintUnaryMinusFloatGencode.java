/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.CompilerOptions;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class ManualTestPrintUnaryMinusFloatGencode {

    public static AbstractProgram initTest1() {
        SymbolTable st = new SymbolTable();
        Type type = new FloatType(st.create("float"));
        ListInst linst = new ListInst();
        ListExpr exp1 = new ListExpr();
        linst.add(new Print(false,exp1));
        // -(5. + 2.4) * 3. = -22.2
        UnaryMinus p1 = new UnaryMinus(
                new Plus(
                        new FloatLiteral(5.0f),
                        new FloatLiteral(2.4f)));
        Multiply p2 = new Multiply(
                p1,
                new FloatLiteral(3));
        p1.setType(type);
        p2.setType(type);
        exp1.add(p2);
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
        // source.prettyPrint(System.out);
        //System.out.println("---- We generate the following assembly code ----");
        String result = gencodeSource(source);
        System.out.println(result);

    }



    public static void main(String args[]) {
        test1();
    }
}
