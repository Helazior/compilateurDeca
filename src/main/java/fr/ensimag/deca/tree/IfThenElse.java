package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.List;

import fr.ensimag.deca.codegen.RegisterManager;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl60
 * @date 01/01/2022
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addComment("Start IF");
        // TODO :  avec l'extension, nommer les labels différemment
        String elseLabel = "else_" + compiler.getNumIf();
        String endIfLabel = "end_if_" + compiler.getNumIf();
        compiler.incrementNumIf();

        RegisterManager regMan = compiler.getRegMan();

        compiler.addComment("cond IF");
        condition.codeGenExpr(compiler);
        // résultat de la condition dans la pile
        regMan.pop(Register.R1);
        // On saute au label else_n si la condition == 0
;
        compiler.addInstruction(new BEQ(new Label(elseLabel)));
        // On execute la thenBranch
        compiler.addComment("IF then_body");
        for (AbstractInst inst:thenBranch.getList()) {
            inst.codeGenInst(compiler);
        }

        // On saute au label end_if_n à la fin du if, juste avant le else
        compiler.addInstruction(new BRA(new Label(endIfLabel)));
        // On met le label else_n
        compiler.addLabel(new Label(elseLabel));
        // On execute la elseBranch
        compiler.addComment("IF else_body");
        for (AbstractInst inst:elseBranch.getList()) {
            inst.codeGenInst(compiler);
        }
        // On met le label end_if_n
        compiler.addComment("FI");
        compiler.addLabel(new Label(endIfLabel));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if(");
        condition.decompile(s);
        s.println("){");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
