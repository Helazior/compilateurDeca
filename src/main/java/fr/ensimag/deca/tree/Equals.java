package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 *
 * @author Clément
 * @date 11/01/2022
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        compiler.addInstruction(new CMP(register0, register1));
        if (!compiler.getIsInNotOp()) {
            compiler.addInstruction(new SEQ(register1));
        } else {
            compiler.addInstruction(new SNE(register1));
        }
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }

}
