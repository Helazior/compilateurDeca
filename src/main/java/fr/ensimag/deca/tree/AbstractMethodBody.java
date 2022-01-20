package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tree.AbstractIdentifier;

public abstract class AbstractMethodBody extends Tree {

    protected abstract void codeGenMethod(DecacCompiler compiler);

    protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp methodEnv,
        AbstractIdentifier currentClass, Type returnType) throws ContextualError;


}
