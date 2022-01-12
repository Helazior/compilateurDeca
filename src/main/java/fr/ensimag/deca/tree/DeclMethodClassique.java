package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.jar.Attributes.Name;

import org.apache.commons.lang.Validate;

/**
 * Délcaration d'une méthode
 * 
 * @author gl60
 * @date 01/01/2022
 */

// CRÉÉE

public class DeclMethodClassique extends AbstractDeclMethod{

    
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListParams parametres;
    private MethodBody corps;


    public DeclMethodClassique(AbstractIdentifier type, AbstractIdentifier name, ListParams parametres, MethodBody corps) {
        this.type = type;
        this.name = name;
        this.parametres = parametres;
        this.corps = corps;
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
