package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }



    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if((leftType.isInt() || leftType.isFloat()) && rightType.sameType(leftType)){
            setType(compiler.getType("boolean"));

        } else if(leftType.isInt() && rightType.isFloat()){
            ConvFloat left = new ConvFloat(getLeftOperand());
            left.setType(compiler.getType("float"));
            setLeftOperand(left);

            setType(compiler.getType("boolean"));

        } else if(leftType.isFloat() && rightType.isInt()){
            ConvFloat right = new ConvFloat(getRightOperand());
            right.setType(compiler.getType("float"));
            setLeftOperand(right);

            setType(compiler.getType("boolean"));

        } else{
            throw new ContextualError("Error: Unsupported operands. Expected : int or float", getLocation());
        }
        return getType();
    }
}
