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
public class Param extends Tree{

    
    private AbstractIdentifier type;
    private AbstractIdentifier name;

    public Param(AbstractIdentifier type, AbstractIdentifier name) {
        this.type = type;
        this.name = name;
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
