package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public abstract class AbstractDeclParam extends Tree{

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the
     * method is OK looking at method body and field initialization.
     */
    protected abstract Type verifyParamSignature(DecacCompiler compiler) throws ContextualError;


    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the method are OK.
     */
    protected abstract void verifyParamType(DecacCompiler compiler, EnvironmentExp methodEnv) throws ContextualError;

    public abstract Symbol getName();
}
