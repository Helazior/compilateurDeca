package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.InlinePortion;

import java.io.PrintStream;

public class MethodAsmBody extends AbstractMethodBody{

    private String corps;
    private Location loc;


    public MethodAsmBody(String text, Location location) {
        this.corps = text;
        this.loc = location;
    }


    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp methodEnv,
            ClassDefinition currentClass, Type returnType) throws ContextualError{
        //nothing
    }



    protected void codeGenMethod(DecacCompiler compiler, ListDeclParam args,
            AbstractIdentifier listDeclField) throws UnsupportedOperationException{
        compiler.add(new InlinePortion(
            corps.substring(1, corps.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\")));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println();
        s.println("asm(" + corps + " );");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    }
}
