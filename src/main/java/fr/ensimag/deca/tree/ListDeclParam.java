package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author gl60
 * @date 14/01/2022
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    //TODO

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam declParam : getList()) {
            declParam.decompile(s);
            s.print(", ");
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
    public void verifyListParamType(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


}


