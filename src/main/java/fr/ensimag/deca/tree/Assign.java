package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));
        getRightOperand().verifyRValue(compiler, localEnv, currentClass, getType());

        return getType();
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        compiler.addComment("Assign");
        getRightOperand().codeGenExpr(compiler);
        compiler.addComment("Store assign");
        getLeftOperand().codeGenStoreLValue(compiler);
        compiler.addComment("End assign");
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
