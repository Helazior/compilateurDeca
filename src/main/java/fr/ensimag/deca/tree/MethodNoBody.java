package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class MethodNoBody extends AbstractMethodBody{


    public MethodNoBody() {
    }


    @Override
    public void decompile(IndentPrintStream s) {
    }    

    @Override
    protected void iterChildren(TreeFunction f) {
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp methodEnv,
            AbstractIdentifier currentClass, Type returnType) throws ContextualError {
        throw new UnsupportedOperationException("blbl");
    }

    /**
     * Write all the code of a method
     * @param compiler
     */
    @Override
    protected void codeGenMethod(DecacCompiler compiler, ListDeclParam args)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("blbl");
    }
}

