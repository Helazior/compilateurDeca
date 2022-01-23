package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;

import java.io.PrintStream;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.getType("float"));
        return getType();
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        RegisterManager regMan = compiler.getRegMan();
        compiler.setIoExistTrue();
        compiler.addInstruction(new RFLOAT());
        compiler.addInstruction(new BOV(new Label("io..error")));
        regMan.push(Register.R1);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
