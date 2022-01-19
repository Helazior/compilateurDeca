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
public class New extends AbstractExpr {
    private AbstractIdentifier NomDeClasse;

    public New(AbstractIdentifier i) {   
        this.NomDeClasse = i;
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
        s.print("new ");
        NomDeClasse.decompile(s);
        s.print("()");
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        NomDeClasse.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        NomDeClasse.prettyPrint(s, prefix, true);
    }

}

