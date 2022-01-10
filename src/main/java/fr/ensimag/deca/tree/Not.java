package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        // TODO: pas d'instrcution not, faut faire comment ?
        // TODO : En mettant 0 si 1 et 1 si 0 ?
        // Faire : 1 xor ... ?
        throw new UnsupportedOperationException("LOL PTDR JE FÉ COMMEN ?");
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
