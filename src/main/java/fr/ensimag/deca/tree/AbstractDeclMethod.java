package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

/**
 * Class declaration.
 *
 * @author gl60
 * @date 12/01/2022
 */
public abstract class AbstractDeclMethod extends Tree {

    /**
     * Write all the code of a method
     * @param compiler
     * @param currentClass
     * @param listDeclField
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler, AbstractIdentifier currentClass, AbstractIdentifier classe) throws DecacFatalError;

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the
     * method is OK looking at method body and field initialization.
     */
    protected abstract void verifyMethodSignature(DecacCompiler compiler, ClassDefinition superClass,
    ClassDefinition currentClass) throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the method are OK.
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler,
        ClassDefinition currentClass) throws ContextualError;

    public abstract AbstractIdentifier getName();
}

