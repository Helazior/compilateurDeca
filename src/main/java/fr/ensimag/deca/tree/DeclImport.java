package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl60
 * @date 12/01/2022
 */
public class DeclImport {
    private String address;
    
    public DeclImport (String address){
        this.address = address;
    }
}
