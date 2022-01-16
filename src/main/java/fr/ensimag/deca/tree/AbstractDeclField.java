package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Class declaration.
 *
 * @author gl60
 * @date 14/01/2022
 */
public abstract class AbstractDeclField extends Tree {
//TODO
    
    protected abstract void verifyField(DecacCompiler compiler)
            throws ContextualError;
}

