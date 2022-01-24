package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;

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
    public void codeGenOp(DecacCompiler compiler) throws DecacFatalError {
        if (!compiler.getIsInNotOp()) { // AND
            super.codeGenAnd(compiler);
        } else { // OR si on utilise le not
            super.codeGenOr(compiler);
        }
    }


    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
