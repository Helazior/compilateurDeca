package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

import static fr.ensimag.ima.pseudocode.Register.R1;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public void codeGenOvError(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        if (!compiler.getCompilerOptions().getNoCheck() && getType().isFloat()) {
            compiler.setOpOvExist();
            compiler.addInstruction(new BOV(new Label("overflow..error")));
        }
        codeGenOp(compiler, register0, register1);
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) throws DecacFatalError {
        codeGenExpr(compiler);
        compiler.getRegMan().pop(R1);
        Type type = getType();
        if (type.isInt()) {
            compiler.addInstruction(new WINT());
        } else if(type.isFloat()) {
            if (printHex) {
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WFLOAT());
            }
        }
        //throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if(leftType.isInt() && rightType.isInt()){
            setType(compiler.getType("int"));
        }
        else if(leftType.isInt() && rightType.isFloat()){
            ConvFloat left = new ConvFloat(getLeftOperand());
            left.setType(compiler.getType("float"));
            setLeftOperand(left);

            setType(compiler.getType("float"));
        }
        else if(leftType.isFloat() && rightType.isInt()){
            ConvFloat right = new ConvFloat(getRightOperand());
            right.setType(compiler.getType("float"));
            setRightOperand(right);

            setType(compiler.getType("float"));
        }
        else if(leftType.isFloat() && rightType.isFloat()){
            setType(compiler.getType("float"));
        }
        else {
            throw new ContextualError("Error: Unsupported operands. Expected : int or float", getLocation());
        }

        return getType();
    }
}
