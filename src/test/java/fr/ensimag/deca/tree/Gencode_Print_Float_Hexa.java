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

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class Gencode_Print_Float_Hexa {

    public static AbstractProgram initTest1() {
        ListInst linst = new ListInst();
        AbstractProgram source =
            new Program(
                new ListDeclClass(),
                new Main(new ListDeclVar(),linst));
        ListExpr lexp1 = new ListExpr(), lexp2 = new ListExpr();
        linst.add(new Println(true,lexp1));
        linst.add(new Print(true,lexp2));
        lexp1.add(new FloatLiteral(3));
        lexp2.add(new FloatLiteral(-4));
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
        /*
        assert(result.equals(
                "; Main program\n" +
                "; Beginning of main function:\n" +
                "	WSTR \"Hello \"\n" +
                "	WSTR \"everybody !\"\n" +
                "	WNL\n" +
                "	HALT\n"));

         */
    }



    public static void main(String args[]) throws ContextualError, DecacFatalError {
        test1();
    }
}
