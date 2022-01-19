package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;

/**
 * Class declaration.
 *
 * @author gl60
 * @date 14/01/2022
 */
public abstract class AbstractDeclField extends Tree {
    //TODO

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the
     * method is OK looking at method body and field initialization.
     */
    protected abstract FieldDefinition verifyFieldVisibility(DecacCompiler compiler,
        AbstractIdentifier superClass, AbstractIdentifier currentClass)
        throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the method are OK.
     */
    protected abstract void verifyFieldType(DecacCompiler compiler,
        EnvironmentExp localEnv, AbstractIdentifier currentClass)
        throws ContextualError;

}

