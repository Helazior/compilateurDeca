package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;

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
            setType(compiler.getType("int"));
        }else if(opType.isFloat()){
            setType(compiler.getType("float"));
        } else {
            throw new ContextualError("Error: Unsupported operands. Expected : int or float", getLocation());
        }
        return getType();
}

    @Override
    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        Type type = getType();
        if (type.isInt()) {
            compiler.addInstruction(new LOAD(0, register1));
        } else if (type.isFloat()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0.0f), register1));
        }
        compiler.addInstruction(new SUB(register0, register1));
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
}
