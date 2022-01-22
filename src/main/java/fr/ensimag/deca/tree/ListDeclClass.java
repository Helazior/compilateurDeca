package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
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
     * Pass 0
     * It's goal is to define a link between the classes and their
     * affilied Node in the Tree
     * We can then go through the classes in the parental order
     * instead of the tree order
     */
    void loadListClassNodes(DecacCompiler compiler) throws ContextualError{
        for (AbstractDeclClass declClass : getList()) {
            declClass.loadClassNodes(compiler);
        }
    }

    protected void codeGenListClass(DecacCompiler compiler) throws DecacFatalError {
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
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClass(compiler);
        }
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

    protected void codeGenListClass(DecacCompiler compiler) throws DecacFatalError {
        for (AbstractDeclClass declClass : getList()) {
            declClass.codeGenClass(compiler);
        }
        // On met la méthode object
        objectEquals.methodEquals(compiler);
    }
}
