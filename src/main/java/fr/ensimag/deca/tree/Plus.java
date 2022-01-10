package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        AbstractExpr left = getLeftOperand();
        left.codeGenExpr(compiler);
        AbstractExpr right = getRightOperand();
        right.codeGenExpr(compiler);
        compiler.addInstruction(new POP(Register.R0));
        compiler.addInstruction(new POP(Register.R1));
        compiler.addInstruction(new ADD(Register.R0, Register.R1));
        compiler.addInstruction(new PUSH(Register.R1));
        //compiler.addInstruction(compiler, getLeftOperand(), getRightOperand());
    }
    @Override
    protected String getOperatorName() {
        return "+";
    }
}
