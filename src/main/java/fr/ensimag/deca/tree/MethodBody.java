package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class MethodBody extends AbstractMethodBody{
    //TODO
    @Override
    protected void verifyMethodBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler) {

        //compiler.getRegMan().declareVars(declVariables);

        compiler.addComment("Beginning of method instructions:");
        //insts.codeGenListInst(compiler);
    }

    /**
    * Contains no real information => nothing to check.
    */
    @Override
    protected void checkLocation() {
        throw new UnsupportedOperationException("not yet implemented");    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("not yet implemented");    }
}
