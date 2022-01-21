package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import sun.java2d.pipe.SpanClipRenderer;

import java.io.PrintStream;

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
    protected void codeGenExpr(DecacCompiler compiler){
        RegisterManager regMan = compiler.getRegMan();
        // on push tous les registres dans la pile pour qu'ils ne gênent pas pendant le calcul d'argument
        objet.codeGenExpr(compiler);
        for (AbstractExpr field : parametres.getList()) {
            field.codeGenExpr(compiler);
        }
        // Il y a this en plus
        regMan.prepareMethodCall(parametres.size() + 1);

        // On saute au label de la méthode
        compiler.addInstruction(new BSR(new Label("bodyMethod." + getClass().getName() + "." + nomDeMethode)));

        // On remet la stack comme avant l'appel de méthode
        compiler.addInstruction(new SUBSP(parametres.size() + 2)); // + this * 2: in the bottom and the top

    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) {
        throw new UnsupportedOperationException("Method is not printable");
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

