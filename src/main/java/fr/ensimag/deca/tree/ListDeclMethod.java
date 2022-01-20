package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.IMAProgram;
import org.apache.log4j.Logger;

/**
 *
 * @author gl60
 * @date 14/01/2022
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    //TODO
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListMethodSignature(DecacCompiler compiler, AbstractIdentifier superClass,
            AbstractIdentifier currentClass) throws ContextualError {

        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.verifyMethodSignature(compiler, superClass, currentClass);
        }
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListMethodBody(DecacCompiler compiler,
        AbstractIdentifier currentClass) throws ContextualError {

        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.verifyMethodBody(compiler, currentClass);
        }
    }

    public void codeGenListDeclMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod declMethod : getList()) {
            IMAProgram newProgram = new IMAProgram();
            // On écrit la méthode dans un nouveau programme. Plus facile pour les addFirst
            IMAProgram oldProgram = compiler.remplaceProgram(newProgram);
            // On écrit notre méthode
            declMethod.codeGenDeclMethod(compiler);
            // On rajoute notre nouveau programme à la fin de l'ancien.
            // Le nouveau programme contiendra tous les programmes
            compiler.concatenateProgram(oldProgram);
        }
    }
}
