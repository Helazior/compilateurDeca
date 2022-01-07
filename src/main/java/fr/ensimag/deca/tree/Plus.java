package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class Plus extends AbstractOpArith implements InterfaceCodePlusOr {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
    //public void codeGenPlus(DecacCompiler compiler) {
        //compiler.addInstruction(new ADD(Register.R0, Register.R1));
        codeGenPlusOr(compiler, getLeftOperand(), getRightOperand());
    }
    @Override
    protected String getOperatorName() {
        return "+";
    }
}
