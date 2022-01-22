package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        // On indique qu'on veut placer le label d'erreur à la fin pour la division, car la division exist
        compiler.setDivideExistTrue();
        Type type = getType();
        if (type.isInt()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new CMP(0, register0));
                compiler.addInstruction(new BEQ(new Label("div_by_zero_error")));
            }
            compiler.addInstruction(new QUO(register0, register1));


        } else if (type.isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new CMP(new ImmediateFloat(0f), register0));
                compiler.addInstruction(new BEQ(new Label("div_by_zero_error")));
            }
            compiler.addInstruction(new DIV(register0, register1));
        }
    }

    /**
     *
     * Generate code to print the expression
     *
     * @param compiler
     * @param printHex
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) throws DecacFatalError {
        super.codeGenPrint(compiler, printHex);
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

}
