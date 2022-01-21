package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Class declaration.
 *
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractDeclClass extends Tree {

    /**
     * Pass 0
     * Link the className to its Node in the Tree
     */
    protected abstract void loadClassNodes(DecacCompiler compiler)
                throws ContextualError;

    /**
     * Pass 1 of [SyntaxeContextuelle]. Verify that the class declaration is OK
     * without looking at its content.
     */
    protected abstract void verifyClass(DecacCompiler compiler)
            throws ContextualError;

    protected abstract void codeGenClass(DecacCompiler compiler) throws ContextualError;

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the class members (fields and
     * methods) are OK, without looking at method body and field initialization.
     */
    protected abstract void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the class are OK.
     */
    protected abstract void verifyClassBody(DecacCompiler compiler)
            throws ContextualError;

    public abstract int codeGenClassTableFn(DecacCompiler compiler, IMAProgram program, int stackPos);

    public abstract void codeGenClassTableMain(DecacCompiler compiler, IMAProgram program);
}
