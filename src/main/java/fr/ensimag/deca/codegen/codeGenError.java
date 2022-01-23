package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

public class codeGenError {

    static public void divByZeroError(DecacCompiler compiler) {
        //compiler.addComment("Manage division by zero error");
        compiler.addLabel(new Label("div..by_zero_error"));
        compiler.addInstruction(new WSTR("Error: Division by zero"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

    }

    static public void modByZeroError(DecacCompiler compiler) {
        //compiler.addComment("Manage division by zero error");
        compiler.addLabel(new Label("mod..by_zero_error"));
        compiler.addInstruction(new WSTR("Error: Modulo by zero"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    static public void ioError(DecacCompiler compiler) {
        /*
        io_error:
            WSTR "Error: Input/Output error"
            WNL
            ERROR
        */
        //compiler.addComment("Manage io error");
        compiler.addLabel(new Label("io..error"));
        compiler.addInstruction(new WSTR("Error: Input/Output error"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    static public void overflowError(DecacCompiler compiler) {
        /*
        overflow_error:
            WSTR "Error: Overflow during arithmetic operation"
            WNL
            ERROR
        */
        //compiler.addComment("Manage ov error");
        compiler.addLabel(new Label("overflow.error"));
        compiler.addInstruction(new WSTR("Error: Overflow during arithmetic operation"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    static public void stackOverflowError(DecacCompiler compiler) {
        compiler.addLabel(new Label("stack..overflow_error"));
        compiler.addInstruction(new WSTR("Error: Stack Overflow"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }


    static public void noReturnError(DecacCompiler compiler) {
        compiler.addLabel(new Label("no..return_error"));
        compiler.addInstruction(new WSTR("Error: Expected a return at the and of a no void method"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    static public void derefNullError(DecacCompiler compiler) {
        compiler.addLabel(new Label("dereferencement..null"));
        compiler.addInstruction(new WSTR("Erreur : dereferencement de null"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }
}
