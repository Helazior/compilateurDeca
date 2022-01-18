package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl60
 * @date 12/01/2022
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        compiler.addInstruction(new CMP(Register.R0, Register.R1));
        if (!compiler.getIsInNotOp()) {
            compiler.addInstruction(new SNE(Register.R0));
        } else { // si NOT devant
            compiler.addInstruction(new SEQ(Register.R0));
        }
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
