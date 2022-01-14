package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type opType = getOperand().verifyExpr(compiler, localEnv, currentClass);

        if(opType.isBoolean()){
            setType(compiler.getType("boolean"));
        }else {
            throw new ContextualError("Error: Unsupported operands. Expected : boolean", getLocation());
        }
        return getType();
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        RegisterManager regMan = compiler.getRegMan();
        Type type = getType();
        if (type.isInt()) {
            compiler.addInstruction(new LOAD(1, Register.R1));
        } else if (type.isFloat()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(1.0f), Register.R1));
        }
        compiler.addInstruction(new SUB(Register.R0, Register.R1));
        regMan.push(Register.R1);
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
}
