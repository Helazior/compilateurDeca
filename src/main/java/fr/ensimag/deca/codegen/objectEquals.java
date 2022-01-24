package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class objectEquals {
    public static void methodEquals(DecacCompiler compiler) throws DecacFatalError {

        compiler.addComment("-------------------------------------------------");
        compiler.addComment("                  Classe Object");
        compiler.addComment("-------------------------------------------------");
        compiler.addLabel(new Label("init.Object"));
        compiler.addComment("----------- Initialisation des champs de Object");
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, Register.R1)));
        compiler.addInstruction(new RTS());

        compiler.addLabel(new Label("methodBody.Object.equals"));
        compiler.addComment("----------- Code de la methode equals dans la classe Object");
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
        // On empile tous les registres qu'on veut utiliser au début de la méthode et on les restaure à la fin

        // goto erreur return en cas de non return
        // On return
        compiler.addInstruction(new RTS());

        compiler.addFirst(new Line("# start Object"));
        compiler.addLine(new Line("# end Object"));
    }
}
