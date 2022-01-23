package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;

/**
 * Class declaration.
 *
 * @author gl60
 * @date 14/01/2022
 */
public abstract class AbstractDeclImport extends Tree {

    /**
     * Pass 0
     * Link the className to its Node in the Tree
     */
    protected abstract void loadImportNodes(DecacCompiler compiler)
            throws ContextualError;

    public abstract AbstractProgram getProgram();
}