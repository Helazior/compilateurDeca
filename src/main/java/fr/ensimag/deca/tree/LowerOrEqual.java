package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SGT;
import fr.ensimag.ima.pseudocode.instructions.SLE;

/**
 *
 * @author gl60
 * @date 12/01/2022
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public void codeGenOpIneq(DecacCompiler compiler, GPRegister register) {
        if (!compiler.getIsInNotOp()) {
            compiler.addInstruction(new SLE(register));
        } else {
            compiler.addInstruction(new SGT(register));
        }
    }

    @Override
    protected String getOperatorName() {
        return "<=";
    }

}
