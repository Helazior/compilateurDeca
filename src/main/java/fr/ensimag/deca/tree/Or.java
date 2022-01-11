package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    @Override
    protected String getOperatorName() {
        return "||";
    }


}
