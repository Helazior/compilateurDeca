package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;
import java.util.jar.Attributes.Name;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;

import org.apache.log4j.Logger;

/**
 *
 * @author gl60
 * @date 12/01/2022
 */
public class DeclImport extends AbstractDeclImport {
    private String address;
    private AbstractProgram program;

    public DeclImport (String address, AbstractProgram program){
        this.address = address;
        this.program = program;
    }

    public void decompile(IndentPrintStream s) {
        s.print("import ");
        s.println(address );
    }

    @Override
    protected void loadImportNodes(DecacCompiler compiler) throws ContextualError{
        program.loadNodes(compiler);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        program.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        program.prettyPrint(s, prefix, true);
    }
}
