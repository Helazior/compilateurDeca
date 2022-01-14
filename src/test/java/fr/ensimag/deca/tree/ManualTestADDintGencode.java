/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.CompilerOptions;
/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class ManualTestADDintGencode {

    public static AbstractProgram initTest1() {
        ListInst linst = new ListInst();
        //linst.add(new Print(false,lexp1));
        linst.add(new Plus(new Plus(new IntLiteral(3), new IntLiteral(2)), new IntLiteral(4)));
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
        System.out.println("---- From the following Abstract Syntax Tree ----");
        source.prettyPrint(System.out);
        System.out.println("---- We generate the following assembly code ----");
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



    public static void main(String args[]) {
        test1();
    }
}
