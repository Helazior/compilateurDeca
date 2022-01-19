package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractMethodBody extends Tree{


    protected void verifyMethodBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Write all the code of a method
     * @param compiler
     */
    protected void codeGenMethod(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
