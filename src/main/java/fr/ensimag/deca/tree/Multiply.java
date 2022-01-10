package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import static fr.ensimag.ima.pseudocode.Register.R1;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        compiler.addInstruction(new MUL(Register.R0, Register.R1));
    }

    /**
     *
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
        return "*";
    }

}