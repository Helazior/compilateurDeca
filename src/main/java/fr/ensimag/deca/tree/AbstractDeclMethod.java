package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Class declaration.
 *
 * @author gl60
 * @date 12/01/2022
 */
public abstract class AbstractDeclMethod extends Tree {
//TODO
    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the
     * method is OK looking at method body and field initialization.
     */
    protected abstract void verifyMethodSignature(DecacCompiler compiler, AbstractIdentifier superClass,
    AbstractIdentifier currentClass) throws ContextualError;

        /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the method are OK.
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler,
        AbstractIdentifier currentClass) throws ContextualError;

}

