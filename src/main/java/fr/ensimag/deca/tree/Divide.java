package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        // TODO : COMMENT ON VÉRIFIE QUE R1 N'EST PAS 0 ? Dans l'assembleur ?
        Type type = getType();
        if (type.isInt()) {
            compiler.addInstruction(new CMP(0, Register.R0));
            compiler.addInstruction(new BEQ(new Label("div_by_zero_error")));
            compiler.addInstruction(new QUO(Register.R0, Register.R1));


        } else if (type.isFloat()) {
            compiler.addInstruction(new CMP(new ImmediateFloat(0f), Register.R0));
            compiler.addInstruction(new BEQ(new Label("div_by_zero_error")));
            compiler.addInstruction(new DIV(Register.R0, Register.R1));
        }
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
        return "/";
    }

}
