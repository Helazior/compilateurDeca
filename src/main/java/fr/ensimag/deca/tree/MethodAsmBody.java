package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class MethodAsmBody extends AbstractMethodBody{

    private String corps;
    private Location loc;


    public MethodAsmBody(String text, Location location) {
        this.corps = text;
        this.loc = location;
    }


    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp methodEnv,
            AbstractIdentifier currentClass, Type returnType) throws ContextualError{
        throw new ContextualError("not yet implemented", getLocation());
    }



    protected void codeGenMethod(DecacCompiler compiler,
            ListDeclParam args) throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println(loc + " : (" + corps + " )");
            
    }    

    @Override
    protected void iterChildren(TreeFunction f) {
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    }
}
