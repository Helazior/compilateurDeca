package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Empty MethodBody Deca program
 *
 * @author gl60
 * @date 14/01/2022
 */
public class EmptyMethodBody extends AbstractMethodBody {
    //TODO
    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp methodEnv,
        ClassDefinition currentClass, Type returnType) throws ContextualError {
        // Wow everything's good !
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler,
                                 ListDeclParam args, ListDeclField listDeclField) throws UnsupportedOperationException {
        // Nothing to do
    }

    /**
     * Contains no real information => nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // no main program => nothing
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
}
