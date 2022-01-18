package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * @author gl60
 * @date 01/01/2022
 */
public class Return extends AbstractInst {
    
    AbstractExpr resultat;

    public Return(AbstractExpr e) {
        this.resultat = e
    }

}

