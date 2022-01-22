package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    /**
     * codeGen de l'opération &&
     * @param compiler
     */
    @Override
    public void codeGenOp(DecacCompiler compiler, GPRegister register1) throws DecacFatalError {
        if (!compiler.getIsInNotOp()) { // AND
            super.codeGenAnd(compiler, register1);
        } else { // OR si on utilise le not
            super.codeGenOr(compiler, register1);
        }
    }


    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
