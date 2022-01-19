package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class MethodBody extends AbstractMethodBody{

    private ListDeclVar declVariables;
    private ListInst insts;


    public MethodBody(ListDeclVar declVariables, ListInst insts) {
        this.declVariables = declVariables;
        this.insts = insts;
    }
    
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
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");    
    }    

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
