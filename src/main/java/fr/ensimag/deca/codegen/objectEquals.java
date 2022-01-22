package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class objectEquals {
    public static void methodEquals(DecacCompiler compiler) throws DecacFatalError {

        // ________________________corps du programme___________________________
        compiler.addComment("------ Code d'Equals dans la classe Object ------");
        //    public boolean equals (Object other) {
        //        return this == other;
        //    }

        compiler.addComment("Beginning of method instructions:");
        // return
        // On récupère le this
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.LB), Register.R1));

        compiler.addInstruction(new CMP(Register.R0, Register.R1));
        compiler.addInstruction(new SEQ(Register.R1));
        //________________________
        // On revient placer ce qu'il manque avec les infos du prog
        // Début de la méthode = label du nom de la méthode
        compiler.addFirst(new Line(new Label("methodBody.object.equals")));
        // On empile tous les registres qu'on veut utiliser au début de la méthode et on les restaure à la fin

        // goto erreur return en cas de non return
        // On return
        compiler.addInstruction(new RTS());
    }
}
