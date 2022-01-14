package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.SGT;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl60
 * @date 12/01/2022
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOpIneq(DecacCompiler compiler) {
        compiler.addInstruction(new SGT(Register.R1));
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }

}
