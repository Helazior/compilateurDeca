package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.codegen.objectEquals;
import fr.ensimag.ima.pseudocode.IMAProgram;
import org.apache.log4j.Logger;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass declClass : getList()) {
            declClass.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClass(compiler);
        }
    }

    protected void codeGenListClass(DecacCompiler compiler) throws ContextualError {
        // On met la méthode object
        objectEquals.methodEquals(compiler);
        IMAProgram newProgram = new IMAProgram();
        // On écrit la méthode dans un nouveau programme. Plus facile pour les addFirst
        IMAProgram oldProgram = compiler.remplaceProgram(newProgram);
        // On met les classes
        for (AbstractDeclClass declClass : getList()) {
            declClass.codeGenClass(compiler);
        }
        // On rajoute notre nouveau programme à la fin de l'ancien.
        // Le nouveau programme contiendra tous les programmes
        compiler.concatenateBeginningProgram(oldProgram);
    }
    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClassMembers(compiler);
        }
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClassBody(compiler);
        }
    }

}
