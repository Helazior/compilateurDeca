package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * codeGen de l'opération &&
     *
     * @param compiler
     */
    @Override
    public void codeGenOp(DecacCompiler compiler) throws DecacFatalError {
        // TODO :  avec l'extension, nommer les labels différemment
        if (!compiler.getIsInNotOp()) { // OR
            super.codeGenOr(compiler);
        } else { // NOT op -> AND
            super.codeGenAnd(compiler);
        }
    }


    @Override
    protected String getOperatorName() {
        return "||";
    }
}