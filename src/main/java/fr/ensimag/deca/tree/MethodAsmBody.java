package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class MethodAsmBody extends AbstractMethodBody{

    private String corps;
    private Location loc;


    public MethodAsmBody(String text, Location location) {
        this.corps = text;
        this.loc = location;
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
}
