package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl60
 * @date 01/01/2022
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        boolean firstLoop = true;

        for (AbstractExpr expr : getList()) {
            if(firstLoop){
                firstLoop=false;
            }else{
                s.print(",");
            }
            expr.decompile(s);
        }
    }

    public void verifyParams(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Signature sig) throws ContextualError {
        if (getList().size() != sig.size()) {
            throw new ContextualError("Arguments differs in length", getLocation());
        }
        for (int i = 0; i < getList().size(); i++) {
            AbstractExpr expr = getList().get(i);
            Type sigtype = sig.paramNumber(i);
            set(i, expr.verifyRValue(compiler, localEnv, currentClass, sigtype));
        }
    }
}