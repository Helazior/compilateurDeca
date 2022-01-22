package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.codegen.RegisterManager;

import static fr.ensimag.ima.pseudocode.Register.R1;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
        Type actualType = this.verifyExpr(compiler, localEnv, currentClass);
        if(expectedType.sameType(actualType)){
            return this;

        } else if(expectedType.isFloat() && actualType.isInt()){
            ConvFloat convFloat = new ConvFloat(this);
            convFloat.setType(compiler.getType("float"));
            return convFloat;

        } else if(expectedType.isClass() && actualType.isClass()
        && ((ClassType)expectedType).isSubClassOf((ClassType)actualType)) {
            return this;
        } else {
            throw new ContextualError("a " + actualType +
            " can't be assigned to a " + expectedType, getLocation());
        }
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = verifyExpr(compiler, localEnv, currentClass);
        if(!t.isBoolean()) {
            throw new ContextualError("Excepted a boolean as condition", getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     * @param printHex
     */
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) {
        RegisterManager regMan = compiler.getRegMan();
        codeGenExpr(compiler);
        regMan.pop(R1);
        Type exprType = getType();
        if (exprType.isInt()) {
            compiler.addInstruction(new WINT());
        } else if (exprType.isFloat()) {
            if (printHex) {
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WFLOAT());
            }
        } else {
            throw new UnsupportedOperationException("Expr not printable");
        }
    }

    public void codeGenExpr(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Expr not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // assign_expr
        //      → or_expr (
        //      { condition : expression must be a "lvalue" }
        //      '= ' assign_expr
        //      | ε )
        //
        //      -> and....

        // Classes filles de AbstractExpr :
        // AbstractStringLiteral; AbstractBinaryExpr; AbstractReadExpr; AbstractLValue; AbstractUnaryExpr;
        // BooleanLiteral; FloatLiteral; IntLiteral
        // Et ses filles !
        codeGenExpr(compiler);
        compiler.getRegMan().give(compiler.getRegMan().pop());
    }
    

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
