/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;
import fr.ensimag.deca.CompilerOptions;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class ManualTestDeclaGencode {

    public static AbstractProgram initTest1() {
        /*
        ListInst linst = new ListInst();
        AbstractProgram source =
                new Program(
                        new ListDeclClass(),
                        new Main(new ListDeclVar(),linst));
        ListExpr lexp1 = new ListExpr(), lexp2 = new ListExpr();
        linst.add(new Print(false,lexp1));
        linst.add(new Println(false,lexp2));
        lexp1.add(new StringLiteral("\"Hello \""));
        lexp2.add(new StringLiteral("\"everybody !\""));
        return source;

        */
        SymbolTable t = new SymbolTable();
        ListDeclVar vars = new ListDeclVar();
        ListInst instrs = new ListInst();
        vars.add(new DeclVar(
                new Identifier(t.create("int")),
                new Identifier(t.create("a")),
                new NoInitialization()
        ));
        instrs.add(new Assign(new Identifier(t.create("a")), new IntLiteral(1)));
        ListExpr printexpr = new ListExpr();
        instrs.add(new Print(false, printexpr));
        return new Program(
                new ListDeclClass(),
                new Main(vars, instrs) {
                }
        );

    }

    public static String gencodeSource(AbstractProgram source) {
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        source.codeGenProgram(compiler);
        return compiler.displayIMAProgram();
    }

    public static void test1() {
        AbstractProgram source = initTest1();
        //System.out.println("---- From the following Abstract Syntax Tree ----");
        source.prettyPrint(System.out);
        //System.out.println("---- We generate the following assembly code ----");
        String result = gencodeSource(source);
        System.out.println(result);
    }



    public static void main(String[] args) {
        test1();
    }
}
