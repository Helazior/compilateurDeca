package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.apache.commons.lang.Validate;

import javax.naming.AuthenticationNotSupportedException;

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

    public void codeGenExpr(DecacCompiler compiler) {
        RegisterManager regMan = compiler.getRegMan();

        AbstractExpr operand = getOperand();
        operand.codeGenExpr(compiler);

        compiler.addComment(getOperatorName());
        GPRegister register0 = regMan.pop();
        GPRegister register1 = regMan.take();

        codeGenOp(compiler, register0, register1);
        regMan.push(register1);
        regMan.give(register0);
        regMan.give(register1);
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
