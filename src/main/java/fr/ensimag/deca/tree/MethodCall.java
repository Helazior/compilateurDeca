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
public class MethodCall extends AbstractExpr {
    private AbstractExpr objet;
    private AbstractIdentifier nomDeMethode;
    private ListExpr parametres;

    public MethodCall(AbstractExpr e, AbstractIdentifier i, ListExpr p) {   
        this.objet = e;
        this.nomDeMethode = i;
        this.parametres = p;
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
        objet.decompile(s);
        s.print(".");
        nomDeMethode.decompile(s);
        s.print("(");
        parametres.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        objet.iter(f);
        nomDeMethode.iter(f);
        parametres.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        objet.prettyPrint(s, prefix, false);
        nomDeMethode.prettyPrint(s, prefix, false);
        parametres.prettyPrint(s, prefix, true);
    }

}

