package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.*;

/**
 * @author Ensimag
 * @date 01/01/2022
 */
public class LOAD extends BinaryInstructionDValToReg {

    public LOAD(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public LOAD(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

    // TODO: j'ai rajouté ça mais pas sûr que c'est comme ça qu'on charge un float
    public LOAD(float i, GPRegister r) {
        this(new ImmediateFloat(i), r);
    }
}
