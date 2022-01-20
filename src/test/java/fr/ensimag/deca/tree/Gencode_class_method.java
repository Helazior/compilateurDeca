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
public class Gencode_class_method {

    public static AbstractProgram initTest1() {


        SymbolTable symType = new SymbolTable();

        // print(3 + 2 + 5);
        SymbolTable st = new SymbolTable();
        Type int_t = new IntType(st.create("int"));
        ListInst linst = new ListInst();
        ListExpr exp1 = new ListExpr();
        linst.add(new Print(false,exp1));
        Plus p1 = new Plus(new IntLiteral(3), new IntLiteral(2));
        Plus p2 = new Plus(p1, new IntLiteral(4));
        p1.setType(int_t);
        p2.setType(int_t);
        exp1.add(p2);

        AbstractMethodBody methodBody1 = new MethodBody(null, linst);
        ListDeclParam parameters1 = new ListDeclParam();
        AbstractIdentifier method1 = new Identifier(symType.create("firstMethod"));
        AbstractIdentifier returnType1 = new Identifier(symType.create("void"));
        AbstractDeclMethod declMeth1 = new DeclMethod(returnType1, method1, parameters1, methodBody1);
        ListDeclMethod listDeclMeth = new ListDeclMethod();
        listDeclMeth.add(declMeth1);
        ListDeclField listDeclFields = null;
        AbstractIdentifier identiferSuperClass = null;

        SymbolTable symTab = new SymbolTable();
        SymbolTable.Symbol name = symTab.create("firstClass");
        AbstractIdentifier curClass = new Identifier(name);

        AbstractDeclClass abstractDeclClass = new DeclClass(curClass, identiferSuperClass, listDeclFields, listDeclMeth);

        ListDeclClass listDeclClass = new ListDeclClass();
        listDeclClass.add(abstractDeclClass);

        return new Program(
                listDeclClass,
                new EmptyMain());

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
