package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl60
 * @date 01/01/2022
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // TODO :  avec l'extension, nommer les labels différemment
        RegisterManager regMan = compiler.getRegMan();

        String whileLabel = "while_" + compiler.getNumWhile();
        String endWhileLabel = "end_while_" + compiler.getNumWhile();
        compiler.incrementNumWhile();

        compiler.addLabel(new Label(whileLabel));
        // On execute la condition
        condition.codeGenExpr(compiler);
        // résultat de la condition dans la pile
        regMan.pop(Register.R1);

        // On termine le while si condition est fausse -> goto end_while_n
        compiler.addInstruction(new BNE(new Label(endWhileLabel)));

        // corps du while
        for (AbstractInst inst : body.getList()) {
            inst.codeGenInst(compiler);
        }

        // Fin du while
        compiler.addInstruction(new BRA(new Label(whileLabel)));
        compiler.addLabel(new Label(endWhileLabel));
    }
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
