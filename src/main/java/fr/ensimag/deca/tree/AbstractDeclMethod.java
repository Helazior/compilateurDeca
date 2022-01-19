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
     * Write all the code of a method
     * @param compiler
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler);

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
        EnvironmentExp localEnv, AbstractIdentifier currentClass)
        throws ContextualError;

    public abstract AbstractIdentifier getName();
}

