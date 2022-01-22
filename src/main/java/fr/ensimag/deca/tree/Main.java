package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
/**
 * @author gl60
 * @date 01/01/2022
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;

    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        // in `bloc`, with env_exp_sup = {}, env_exp = {}, class = 0, ret = void

        EnvironmentExp envSup = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler, envSup, null);
        insts.verifyListInst(compiler, envSup, null, compiler.getType("void"));

        LOG.debug("verify Main: end");
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) throws DecacFatalError {
        compiler.getRegMan().declareGlobalVars(declVariables, 0); // TODO: set table size

        compiler.addComment("---------------------------------------------------");
        compiler.addComment("          Beginning of main instructions:");
        compiler.addComment("---------------------------------------------------");
        insts.codeGenListInst(compiler);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        s.println();
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
