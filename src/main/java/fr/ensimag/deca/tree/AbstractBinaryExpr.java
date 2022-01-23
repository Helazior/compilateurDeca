package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenOp(DecacCompiler compiler) throws DecacFatalError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenOvError(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        codeGenOp(compiler, register0, register1);
    }

    public void codeGenAnd(DecacCompiler compiler) throws DecacFatalError {
        RegisterManager regMan = compiler.getRegMan();
        compiler.addComment("Start And");
        String falseLabel = "false_and_" + compiler.getNumAnd();
        String endLabel = "end_and_" + compiler.getNumAnd();
        compiler.incrementNumAnd();
        // On teste la condition leftOp
        getLeftOperand().codeGenExpr(compiler);
        // On récupère le résultat de la condition dans la pile
        GPRegister register1 = regMan.pop();
        // 0 : cond false -> goto falseLabel
        compiler.addComment(getOperatorName());
        compiler.addInstruction(new CMP(0, register1));
        compiler.addInstruction(new BEQ(new Label(falseLabel)));
        regMan.give(register1);
        // cond true :
        // On teste rightOp
        getRightOperand().codeGenExpr(compiler);
        register1 = regMan.pop();
        // 0 : cond false -> goto false_and_n
        compiler.addInstruction(new CMP(0, register1));
        compiler.addInstruction(new BEQ(new Label(falseLabel)));
        // load 1 -> goto end_and_n
        compiler.addInstruction(new LOAD(1, register1));
        compiler.addInstruction(new BRA(new Label(endLabel)));
        // lbl false_and_n:
        compiler.addLabel(new Label(falseLabel));
        // load 0
        compiler.addInstruction(new LOAD(0, register1));
        // lbl end_and_n
        compiler.addLabel(new Label(endLabel));
        regMan.giveAndPush(register1);
    }


    public void codeGenOr(DecacCompiler compiler) throws DecacFatalError { // OR si on utilise le not
        RegisterManager regMan = compiler.getRegMan();
        compiler.addComment("Start Or");
        String trueLabel = "true_or_" + compiler.getNumOr();
        String endLabel = "end_or_" + compiler.getNumOr();
        compiler.incrementNumOr();
        // On teste la condition leftOp
        getLeftOperand().codeGenExpr(compiler);
        // On récupère le résultat de la condition dans la pile
        GPRegister register1 = regMan.pop();
        // 1 : cond true -> goto trueLabel
        compiler.addComment(getOperatorName());
        compiler.addInstruction(new CMP(1, register1));
        compiler.addInstruction(new BEQ(new Label(trueLabel)));
        regMan.give(register1);
        // cond false :
        // On teste rightOp
        getRightOperand().codeGenExpr(compiler);
        register1 = regMan.pop();
        // 1 : cond true -> goto true_or_n
        compiler.addInstruction(new CMP(1, register1));
        compiler.addInstruction(new BEQ(new Label(trueLabel)));
        // load 0 -> goto end_or_n
        compiler.addInstruction(new LOAD(0, register1));
        compiler.addInstruction(new BRA(new Label(endLabel)));
        // lbl true_and_n:
        compiler.addLabel(new Label(trueLabel));
        // load 1
        compiler.addInstruction(new LOAD(1, register1));
        // lbl end_and_n
        compiler.addLabel(new Label(endLabel));
        regMan.giveAndPush(register1);
    }


    @Override
    public void codeGenExpr(DecacCompiler compiler) throws DecacFatalError {
        RegisterManager regMan = compiler.getRegMan();
        //super.codeGenExpr(compiler);
        AbstractExpr left = getLeftOperand();
        left.codeGenExpr(compiler);
        //compiler.addComment("");
        AbstractExpr right = getRightOperand();
        right.codeGenExpr(compiler);

        GPRegister registre0 = regMan.pop();
        GPRegister registre1 = regMan.pop();
        compiler.addComment(getOperatorName());
        codeGenOvError(compiler, registre0, registre1);

        regMan.giveAndPush(registre1);
        regMan.give(registre0);
    }



    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

}
