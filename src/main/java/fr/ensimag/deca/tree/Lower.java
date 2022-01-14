package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.SLT;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOpIneq(DecacCompiler compiler) {
        compiler.addInstruction(new SLT(Register.R1));
    }

    @Override
    protected String getOperatorName() {
        return "<";
    }

}
