package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractMethodBody extends Tree{

    protected abstract void codeGenMethod(DecacCompiler compiler);

    protected abstract void verifyMethodBody(DecacCompiler compiler) throws ContextualError;

}
