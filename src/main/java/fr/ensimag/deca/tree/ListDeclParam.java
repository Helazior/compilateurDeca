package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl60
 * @date 14/01/2022
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    @Override
    public void decompile(IndentPrintStream s) {
        boolean firstLoop = true;

        for (AbstractDeclParam declParam : getList()) {
            if(firstLoop){
                firstLoop=false;
            }else{
                s.print(",");
            }
            declParam.decompile(s);
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public Signature verifyListParamSignature(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();

        for (AbstractDeclParam declParam : getList()) {
            sig.add(declParam.verifyParamSignature(compiler));
        }

        return sig;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void  verifyListParamType(DecacCompiler compiler, EnvironmentExp methodEnv) throws ContextualError {

        for (AbstractDeclParam declParam : getList()) {
            declParam.verifyParamType(compiler, methodEnv);
        }
    }
}


