package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.context.ContextualPrintTypeError;

/**
 * Print statement (print, println, ...).
 *
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        for (AbstractExpr expr : arguments.getList()) { // TODO: use insts.iterChildren ?
            Type exprType = expr.verifyExpr(compiler, localEnv, currentClass);
            if (!exprType.isInt() && !exprType.isFloat() && !exprType.isString()) {
                throw new ContextualPrintTypeError(exprType, getLocation());
            }
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // inst
        //  → 'print ' '( ' list_expr ') ' '; '

        // list_expr
        //      → ( expr
        //        ( ',' expr) ∗ ) ?
        // Les filles de AbstractExpr sont :
        // (Done)IntLiteral, (~Done)FloatLiteral, Minus, Plus, Times, AbstractBinaryExpr, AbstractLvalue,
        // AbstractReadExpr, AbstractUnaryExpr, (Done)AbstractStringLiteral
        // + Les petites filles....
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler, getPrintHex());
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print" + getSuffix() + ((printHex)? "x" : "") + "(");
        arguments.decompile(s);
        s.println(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
