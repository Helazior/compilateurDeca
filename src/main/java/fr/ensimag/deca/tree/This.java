package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import org.apache.log4j.Logger;
/**
 * String literal
 *
 * @author gl60
 * @date 17/01/2022
 */
public class This extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(StringLiteral.class);
    //l’attribut est vrai si et seulement si le nœud a été ajouté pendant l’analyse syntaxique sans que le programme source ne contienne le mot clé this (par exemple, m(); pour dire this.m();).
    private boolean ajouteChaine;

    public This(boolean b) {
        Validate.notNull(b);
        this.ajouteChaine = b;
    }

//TODO
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                throw new UnsupportedOperationException("not yet implemented");
        
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) {
        
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
        return "This (" + ")";
    }

}
