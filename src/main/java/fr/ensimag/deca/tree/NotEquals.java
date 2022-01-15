package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl60
 * @date 12/01/2022
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public void codeGenOp(DecacCompiler compiler) {
        // EQUALS
        compiler.addInstruction(new CMP(Register.R0, Register.R1));
        compiler.addInstruction(new SEQ(Register.R0));
        // NOT
        compiler.addInstruction(new LOAD(1, Register.R1));
        compiler.addInstruction(new SUB(Register.R0, Register.R1));
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
