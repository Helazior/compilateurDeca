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
public class ListDeclImport extends TreeList<AbstractDeclImport> {

    //TODO
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclImport declImport : getList()) {
            declImport.decompile(s);
        }
        s.println();
    }

        /**
     * Pass 0
     * It's goal is to define a link between the classes and their
     * affilied Node in the Tree
     * We can then go through the classes in the parental order
     * instead of the tree order
     */
    void loadListImportNodes(DecacCompiler compiler) throws ContextualError{
        for (AbstractDeclImport declImport : getList()) {
            declImport.loadImportNodes(compiler);
        }
    }
}
