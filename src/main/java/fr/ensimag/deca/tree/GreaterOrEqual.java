package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SGE;
import fr.ensimag.ima.pseudocode.instructions.SLT;

/**
 * Operator "x >= y"
 * 
 * @author gl60
 * @date 12/01/2022
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOpIneq(DecacCompiler compiler, GPRegister register) {
        if (!compiler.getIsInNotOp()) {
            compiler.addInstruction(new SGE(register));
        } else {
            compiler.addInstruction(new SLT(register));
        }
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
