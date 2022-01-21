/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable;

import java.util.List;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class Gencode_basic_class {

    public static AbstractProgram initTest1() {
        ListDeclMethod listDeclMeth = null;
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
