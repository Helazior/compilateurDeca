package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractLValue extends AbstractExpr {
    protected abstract void codeGenStoreLValue(DecacCompiler compiler) throws DecacFatalError;
    protected abstract void codeGenGetLValue(DecacCompiler compiler);
}

