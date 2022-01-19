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
public class Selection extends AbstractLValue {
    private AbstractExpr objet;
    private AbstractIdentifier nomDAttribut;

    public Selection(AbstractExpr e, AbstractIdentifier i) {   
        this.objet = e;
        this.nomDAttribut = i;
    }

//TODO
    @Override
    protected void codeGenStoreLValue(DecacCompiler compiler){
    }

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


