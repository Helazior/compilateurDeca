package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.jar.Attributes.Name;

import org.apache.commons.lang.Validate;

import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class DeclVar extends AbstractDeclVar {

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t = type.verifyType(compiler);
        if (t.isVoid()) {
            throw new ContextualError("Cannot have a 'void' type", getLocation());
        }

        Symbol name = varName.getName();
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);

        varName.setDefinition(new VariableDefinition(t, getLocation()));
        try{
            localEnv.declare(name, varName.getVariableDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError(e + " This varibale is already defined in this context", getLocation());
        }
    }

    // TODO: codeGen : utiliser le décorateur Definition.setOperand() pour retrouver facilement l'opérande à utiliser avec getOperand()
    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.print(" ;");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    public AbstractIdentifier codeGenDecl(DecacCompiler compiler, int offset) {
        initialization.codeGenInit(compiler, offset);
        return varName;
    }
}
