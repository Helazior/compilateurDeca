package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;


/**
 * @author gl60
 * @date 12/01/2022
 */

// CRÉÉE

public class DeclMethodAsm extends AbstractDeclMethod{
    
    private String text;
    private Location location;


    public DeclMethodAsm(String text, Location location) { 
        this.text = text;
        this.location = location;
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    }
}
