package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Class declaration.
 *
 * @author gl60
 * @date 12/01/2022
 */
public abstract class AbstractDeclMethod extends Tree {

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the
     * method is OK looking at method body and field initialization.
     */
    protected abstract void verifyMethod(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the method are OK.
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler)
            throws ContextualError;

}

