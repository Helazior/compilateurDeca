package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;

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
    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        /*
        compiler.addInstruction(new LOAD(1, register1));
        compiler.addInstruction(new SUB(register0, register1));
         */
    }

    /**
     * Pour opti le Not
     * @param compiler
     */
    @Override
    public void codeGenExpr(DecacCompiler compiler) throws DecacFatalError {
        codeGenNotExpr(compiler);
    }

    @Override
    public void codeGenNotExpr(DecacCompiler compiler) throws DecacFatalError {
        /*
        RegisterManager regMan = compiler.getRegMan();
        GPRegister register0 = regMan.pop();
        GPRegister register1 = regMan.take();

        codeGenOp(compiler, register0, register1);
        regMan.push(register1);
        regMan.give(register0);
        regMan.give(register1);
         */

        AbstractExpr operand = getOperand();
        // Si on n'est pas déjà dans un NOT, on met le NOT, sinon on annule
        compiler.inverseIsInNotOp();
        operand.codeGenNotExpr(compiler);
        compiler.inverseIsInNotOp();

        compiler.addComment(getOperatorName());

    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
}
