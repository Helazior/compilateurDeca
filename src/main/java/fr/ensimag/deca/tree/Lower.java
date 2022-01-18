package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SGE;
import fr.ensimag.ima.pseudocode.instructions.SLT;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOpIneq(DecacCompiler compiler, GPRegister register) {
        if (!compiler.getIsInNotOp()) {
            compiler.addInstruction(new SLT(register));
        } else {
            compiler.addInstruction(new SGE(register));
        }
    }

    @Override
    protected String getOperatorName() {
        return "<";
    }

}
