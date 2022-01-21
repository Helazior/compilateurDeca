package fr.ensimag.deca.tree;
import java.util.ArrayList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.ima.pseudocode.*;

public abstract class AbstractMethodBody extends Tree {


    protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp methodEnv,
        ClassDefinition currentClass, Type returnType) throws ContextualError;


    /**
     * Write all the code of a method
     * @param compiler
     */
    protected abstract void codeGenMethod(DecacCompiler compiler,
        ListDeclParam args) throws UnsupportedOperationException;
}
