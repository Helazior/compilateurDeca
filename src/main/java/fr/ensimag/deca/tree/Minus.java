package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        compiler.addInstruction(new SUB(register0, register1));
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     * @param printHex
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) throws DecacFatalError {
        super.codeGenPrint(compiler, printHex);
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
    
}
