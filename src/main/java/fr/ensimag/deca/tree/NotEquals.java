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
        compiler.addInstruction(new CMP(register0, register1));
        if (!compiler.getIsInNotOp()) {
            compiler.addInstruction(new SNE(register1));
        } else { // si NOT devant
            compiler.addInstruction(new SEQ(register1));
        }
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
