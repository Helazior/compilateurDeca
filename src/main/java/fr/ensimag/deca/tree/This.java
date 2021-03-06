package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
/**
 * String literal
 *
 * @author gl60
 * @date 17/01/2022
 */
public class This extends AbstractExpr {
    /**
     * l’attribut est vrai si et seulement si le nœud a été ajouté pendant l’analyse syntaxique 
     * sans que le programme source ne contienne le mot clé this (par exemple, m(); pour dire this.m();).
    */

    private boolean ajouteChaine;

    public This(boolean b) {
        Validate.notNull(b);
        this.ajouteChaine = b;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if(currentClass == null){
            throw new ContextualError("Cannot use the symbol 'this' outside a class", getLocation());
        }

        setType(currentClass.getType());
        return getType();
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler){
        RegisterManager regMan = compiler.getRegMan();
        GPRegister reg = regMan.take();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), reg));
        regMan.giveAndPush(reg);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(ajouteChaine));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    String prettyPrintNode() {
        return "This (" + ajouteChaine + ")";
    }

}
