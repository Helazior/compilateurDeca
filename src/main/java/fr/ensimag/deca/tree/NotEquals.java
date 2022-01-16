package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
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

    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        // EQUALS
        compiler.addInstruction(new CMP(register0, register1));
        compiler.addInstruction(new SEQ(register0));
        // NOT
        compiler.addInstruction(new LOAD(1, register1));
        compiler.addInstruction(new SUB(register0, register1));
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
