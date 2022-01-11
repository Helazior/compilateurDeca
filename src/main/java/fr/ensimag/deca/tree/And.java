package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }




    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        // TODO !!!!!!!!!!!!!!!
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
