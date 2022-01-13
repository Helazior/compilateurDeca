package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUB;

import java.awt.image.RescaleOp;

import fr.ensimag.deca.codegen.RegisterManager;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type opType = getOperand().verifyExpr(compiler, localEnv, currentClass);

        if(opType.isInt()){
            return compiler.intType();
        }else if(opType.isFloat()){
            return compiler.floatType();
        } else {
            throw new ContextualError("Error: Unsupported operands. Expected : int or float", getLocation());
        }
}

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        RegisterManager regMan = compiler.getRegMan();
        AbstractExpr operand = getOperand();
        operand.codeGenExpr(compiler);


        regMan.pop(Register.R0);
        Type type = getType();
        if (type.isInt()) {
            compiler.addInstruction(new LOAD(0, Register.R1));
        } else if (type.isFloat()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0.0f), Register.R1));
        }
        compiler.addInstruction(new SUB(Register.R0, Register.R1));
        regMan.push(Register.R1);
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
}
