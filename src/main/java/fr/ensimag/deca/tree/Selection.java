package fr.ensimag.deca.tree;

import com.sun.org.apache.bcel.internal.generic.ArrayElementValueGen;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;

import jdk.jfr.consumer.RecordedThreadGroup;
import org.apache.commons.lang.Validate;

import org.apache.log4j.Logger;
import sun.tools.jconsole.inspector.XObject;

/**
 * String literal
 *
 * @author gl60
 * @date 17/01/2022
 */
public class Selection extends AbstractLValue {
    private AbstractExpr objet;
    private AbstractIdentifier nomDAttribut;

    public Selection(AbstractExpr e, AbstractIdentifier i) {   
        this.objet = e;
        this.nomDAttribut = i;
    }

//TODO

    @Override
    public void codeGenExpr(DecacCompiler compiler) throws ContextualError {
        objet.codeGenExpr(compiler);
        this.nomDAttribut.codeGenSelectIdent(compiler);
    }

    @Override
    protected void codeGenStoreLValue(DecacCompiler compiler) throws ContextualError {
        RegisterManager regMan = compiler.getRegMan();
        GPRegister regResultat = regMan.pop();
        objet.codeGenExpr(compiler);
        regMan.pop(Register.R1);
        regMan.setField(Register.R1, nomDAttribut.getName(), objet.getType(), regResultat, getLocation());
        regMan.give(regResultat);
    }

    protected void codeGenGetLValue(DecacCompiler compiler) throws ContextualError {
        objet.codeGenExpr(compiler);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) {
        super.codeGenPrint(compiler, printHex);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        objet.decompile(s);
        s.print(".");
        nomDAttribut.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        objet.iter(f);
        nomDAttribut.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        objet.prettyPrint(s, prefix, false);
        nomDAttribut.prettyPrint(s, prefix, false);
    }
}


