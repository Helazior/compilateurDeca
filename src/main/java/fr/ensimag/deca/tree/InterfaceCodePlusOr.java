package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;

import java.util.ResourceBundle;

/**
 * The same as the AND : The OR instruction does not exist in our assembly code, we will replace it by a Plus,
 * because the result will be identical. So we use a common interface to `Or` and `Plus`
 * to implement the generation of the 2 instructions.
 */
public interface InterfaceCodePlusOr {

    /**
     * Do the addition between leftOp adn rightOp
     * @param compiler
     * @param leftOp
     * @param rightOp
     */
    default void codeGenPlusOr(DecacCompiler compiler, AbstractExpr leftOp, AbstractExpr rightOp) {
        // TODO : itérer sur leftOp et rightOp et les mettre dans la ram, puis LOAD dans les registres 0 et 1 pour
        // TODO : Comment on itère puisqu'on est en bas de l'arbre.
        compiler.addInstruction(new POP(Register.R0));
        compiler.addInstruction(new POP(Register.R1));
        compiler.addInstruction(new ADD(Register.R0, Register.R1));
        //compiler.addInstruction(new ADD(leftOp, rightOp));
    }
}
