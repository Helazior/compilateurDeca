package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        //codeGenMultiplyAnd(compiler, getLeftOperand(), getRightOperand());
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

}
