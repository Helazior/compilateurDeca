package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author gl60
 * @date 01/01/2022
 *
 */
public abstract class AbstractProgram extends Tree {
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError;
    public abstract void loadNodes(DecacCompiler compiler) throws ContextualError;
    public abstract void codeGenProgram(DecacCompiler compiler) throws DecacFatalError;

    public abstract ListDeclImport getImports();
    public abstract ListDeclClass getClasses();
    public abstract AbstractMain getMain();

}
