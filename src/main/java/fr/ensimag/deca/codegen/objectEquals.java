package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

public class objectEquals {
    public static void methodEquals(DecacCompiler compiler) {

        RegisterManager regMan = compiler.getRegMan();

        // ________________________corps du programme___________________________
        compiler.addComment("------ Code d'Equals dans la classe Object ------");
        // TODO : à revoir
        compiler.addInstruction(new CMP(Register.R0, Register.R1));

        //________________________
        // On revient placer ce qu'il manque avec les infos du prog
        // Début de la méthode = label du nom de la méthode
        compiler.addFirst(new Line(new Label("methodbody.object.equals")));
        // On empile tous les registres qu'on veut utiliser au début de la méthode et on les restaure à la fin
        regMan.restoreRegisters();

        // goto erreur return en cas de non return
        // On return
        compiler.addInstruction(new RTS());
    }
}
