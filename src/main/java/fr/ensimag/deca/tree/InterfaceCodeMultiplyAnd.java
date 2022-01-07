package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.POP;

/**
 * The AND instruction does not exist in our assembly code, we will replace it by a Multiply,
 * because the result will be identical. So we use a common interface to `And` and Multiply
 * to implement the generation of the 2 instructions.
 */
public interface InterfaceCodeMultiplyAnd {
    /**
     * Do the multiplication between leftOp adn rightOp
     * @param compiler
     * @param leftOp
     * @param rightOp
     */
    default void codeGenMultiplyAnd(DecacCompiler compiler, AbstractExpr leftOp, AbstractExpr rightOp) {
        // TODO : mettre leftOp sous forme de registre ou literal, rightOp sous forme de registre
        compiler.addInstruction(new POP(Register.R0));
        compiler.addInstruction(new POP(Register.R1));
        compiler.addInstruction(new MUL(Register.R0, Register.R1));
    }
}
