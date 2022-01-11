package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

public class codeGenError {

    static public void divByZeroError (DecacCompiler compiler)
    {
        // TODO : opti : ne le mettre que si on a une division
        compiler.addComment("Manage division by zero error");
        compiler.addLabel(new Label("div_by_zero_error"));
        compiler.addInstruction(new WSTR("Error: Division by zero"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }
}
