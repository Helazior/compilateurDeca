package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * codeGen de l'opération &&
     *
     * @param compiler
     */
    @Override
    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        // TODO :  avec l'extension, nommer les labels différemment
        RegisterManager regMan = compiler.getRegMan();
        compiler.addComment("Start Or");
        String trueLabel = "true_or_" + compiler.getNumOr();
        String endLabel = "end_or_" + compiler.getNumOr();
        compiler.incrementNumOr();
        // On teste la condition leftOp
        getLeftOperand().codeGenExpr(compiler);
        // On récupère le résultat de la condition dans la pile
        regMan.pop(Register.R1);
        // 1 : cond true -> goto trueLabel
        compiler.addComment(getOperatorName());
        compiler.addInstruction(new CMP(1, Register.R1));
        compiler.addInstruction(new BEQ(new Label(trueLabel)));
        // cond false :
        // On teste rightOp
        getRightOperand().codeGenExpr(compiler);
        regMan.pop(Register.R1);
        // 1 : cond true -> goto true_or_n
        compiler.addInstruction(new CMP(1, Register.R1));
        compiler.addInstruction(new BEQ(new Label(trueLabel)));
        // load 0 -> goto end_or_n
        compiler.addInstruction(new LOAD(0, Register.R1));
        compiler.addInstruction(new BRA(new Label(endLabel)));
        // lbl true_and_n:
        compiler.addLabel(new Label(trueLabel));
        // load 1
        compiler.addInstruction(new LOAD(1, Register.R1));
        // lbl end_and_n
        compiler.addLabel(new Label(endLabel));
        regMan.push(Register.R1);
    }


    @Override
    protected String getOperatorName() {
        return "||";
    }
}