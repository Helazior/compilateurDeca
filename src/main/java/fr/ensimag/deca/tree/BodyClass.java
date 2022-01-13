package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Corps d'une classe (attributs et méthodes).
 *
 * @author gl60
 * @date 12/01/2022
 */

// CRÉÉE

 public class BodyClass extends Tree {

    private ListDeclMethode methodes;
    private ListDeclFieldSet attributs;

    public BodyClass(ListDeclMethode methodes, ListDeclFieldSet attributs) {
        this.methodes = methodes;
        this.attributs = attributs;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }


 }