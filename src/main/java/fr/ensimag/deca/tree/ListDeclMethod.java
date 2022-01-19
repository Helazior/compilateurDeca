package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
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
            EnvironmentExp localEnv, AbstractIdentifier currentClass)
            throws ContextualError {

        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.verifyMethodBody(compiler, localEnv, currentClass);
        }
    }

}
