package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        compiler.addInstruction(new MUL(Register.R0, Register.R1));
    }

    /**
     *
     * Generate code to print the expression
     *
     * @param compiler
     * @param printHex
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) {
        super.codeGenPrint(compiler, printHex);
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

}