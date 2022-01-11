/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import com.sun.org.apache.xpath.internal.operations.Mod;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.CompilerOptions;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class ManualTestPrintMODintGencode {

    public static AbstractProgram initTest1() {
        SymbolTable st = new SymbolTable();
        Type int_t = new IntType(st.create("int"));
        ListInst linst = new ListInst();
        ListExpr exp1 = new ListExpr();
        linst.add(new Print(false,exp1));
        // (27%10)%10 = 7%10 = 7
        Modulo p1 = new Modulo(new IntLiteral(27), new IntLiteral(10));
        Modulo p2 = new Modulo(p1, new IntLiteral(10));
        p1.setType(int_t);
        p2.setType(int_t);
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
