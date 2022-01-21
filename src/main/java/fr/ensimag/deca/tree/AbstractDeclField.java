package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;

/**
 * Class declaration.
 *
 * @author gl60
 * @date 14/01/2022
 */
public abstract class AbstractDeclField extends Tree {
    //TODO

    protected abstract SymbolTable.Symbol getName();

    protected abstract Type getType();

    protected abstract AbstractInitialization getInitialization();

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the
     * method is OK looking at method body and field initialization.
     */
    protected abstract void verifyFieldVisibility(DecacCompiler compiler,
        AbstractIdentifier superClass, AbstractIdentifier currentClass)
        throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the method are OK.
     */
    protected abstract void verifyFieldType(DecacCompiler compiler,
        AbstractIdentifier currentClass) throws ContextualError;

}

