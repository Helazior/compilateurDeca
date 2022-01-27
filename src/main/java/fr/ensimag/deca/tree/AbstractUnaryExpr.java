package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }
    private AbstractExpr operand;
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    public void codeGenOp(DecacCompiler compiler, GPRegister register0, GPRegister register1) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) throws DecacFatalError {
        RegisterManager regMan = compiler.getRegMan();

        operand.codeGenExpr(compiler);

        compiler.addComment(getOperatorName());
        GPRegister register0 = regMan.pop();
        GPRegister register1 = regMan.take();

        codeGenOp(compiler, register0, register1);
        regMan.giveAndPush(register1);
        regMan.give(register0);
    }

    protected abstract String getOperatorName();

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        s.print(" " + getOperatorName() + " ");
        operand.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

}
