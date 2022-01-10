package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
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
    public void codeGenOp(DecacCompiler compiler) {
        compiler.addInstruction(new SUB(Register.R0, Register.R1));
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        super.codeGenPrint(compiler);
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
    
}
