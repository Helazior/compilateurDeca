package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if(leftType.isInt() && rightType.isInt()){
            setType(compiler.getType("int"));
        } else {
            throw new ContextualError("Error: Unsupported operands. Expected : int", getLocation());
        }
        return getType();
    }

    @Override
    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        compiler.setModuloExistTrue();
        Type type = getType();
        if (type.isInt()) {
            compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
            compiler.addInstruction(new BEQ(new Label("mod_by_zero_error")));
            compiler.addInstruction(new REM(Register.R0, Register.R1));
        } else {
            throw new UnsupportedOperationException("Error: modulo with float. Expected : int");
        }
    }


    /*
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        super.codeGenPrint(compiler);
    }*/

    @Override
    protected String getOperatorName() {
        return "%";
    }

}
