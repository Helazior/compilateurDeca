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
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    /**
     * codeGen de l'opération &&
     * @param compiler
     */
    @Override
    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        // TODO :  avec l'extension, nommer les labels différemment
        RegisterManager regMan = compiler.getRegMan();
        compiler.addComment("Start And");
        String falseLabel = "false_and_" + compiler.getNumAnd();
        String endLabel = "end_and_" + compiler.getNumAnd();
        compiler.incrementNumAnd();
        // On teste la condition leftOp
        getLeftOperand().codeGenExpr(compiler);
        // On récupère le résultat de la condition dans la pile
        regMan.pop(Register.R1);
        // 0 : cond false -> goto falseLabel
        compiler.addComment(getOperatorName());
        compiler.addInstruction(new CMP(0, Register.R1));
        compiler.addInstruction(new BEQ(new Label(falseLabel)));
        // cond true :
        // On teste rightOp
        getRightOperand().codeGenExpr(compiler);
        regMan.pop(Register.R1);
        // 0 : cond false -> goto false_and_n
        compiler.addInstruction(new CMP(0, Register.R1));
        compiler.addInstruction(new BEQ(new Label(falseLabel)));
        // load 1 -> goto end_and_n
        compiler.addInstruction(new LOAD(1, Register.R1));
        compiler.addInstruction(new BRA(new Label(endLabel)));
        // lbl false_and_n:
        compiler.addLabel(new Label(falseLabel));
        // load 0
        compiler.addInstruction(new LOAD(0, Register.R1));
        // lbl end_and_n
        compiler.addLabel(new Label(endLabel));
        regMan.push(Register.R1);
    }


    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
