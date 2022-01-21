package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import fr.ensimag.deca.codegen.RegisterManager;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 * @author gl60
 * @date 18/01/2022
 */
public class Return extends AbstractInst {
    AbstractExpr resultat;

    public Return(AbstractExpr e) {
        this.resultat = e;
    }

    //TODO
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        resultat.codeGenExpr(compiler);
        compiler.getRegMan().pop(Register.R1);
        compiler.addInstruction(new BRA(new Label("return" + compiler.getNbReturn())));
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        resultat.decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        resultat.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        resultat.prettyPrint(s, prefix, true);
    }

}

