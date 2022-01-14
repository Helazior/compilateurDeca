package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.Register;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
    ClassDefinition currentClass) throws ContextualError {

        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if(leftType.isClassOrNull() && rightType.isClassOrNull()){
            setType(compiler.getType("boolean"));
        } else if(leftType.isBoolean() && rightType.isBoolean()){
            setType(compiler.getType("boolean"));
        }else{
            setType(super.verifyExpr(compiler, localEnv, currentClass));
        }
        return getType();
    }
}
