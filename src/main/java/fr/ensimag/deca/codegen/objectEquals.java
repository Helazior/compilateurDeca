package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class objectEquals {
    public static void methodEquals(DecacCompiler compiler) throws DecacFatalError {

        RegisterManager regMan = compiler.getRegMan();

        // ________________________corps du programme___________________________
        compiler.addComment("------ Code d'Equals dans la classe Object ------");
        // TODO : à tester
//        public boolean equals (Object other) {
//            return this == other;
//        }

        SymbolTable st = new SymbolTable();
        AbstractDeclParam param = new DeclParam(new Identifier(st.create(new String("Object"))), new Identifier(st.create(new String("other"))));
        ListDeclParam params = new ListDeclParam();
        params.add(param);
        // On déclare le parametre "other"
        compiler.getRegMan().declareMethodVars(params, null);

        compiler.addComment("Beginning of method instructions:");
        // return
        // On récupère le this
        GPRegister regThis = regMan.take();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), regThis));

        // TODO: comment je récupère other ?
        GPRegister regOther = regMan.pop();


        compiler.addInstruction(new CMP(regThis, regOther));
        compiler.addInstruction(new SEQ(Register.R1));
        regMan.give(regOther);
        regMan.give(regThis);
        //________________________
        // On revient placer ce qu'il manque avec les infos du prog
        // Début de la méthode = label du nom de la méthode
        compiler.addFirst(new Line(new Label("methodBody.object.equals")));
        // On empile tous les registres qu'on veut utiliser au début de la méthode et on les restaure à la fin
        regMan.restoreRegisters();

        // goto erreur return en cas de non return
        // On return
        compiler.addInstruction(new RTS());
    }
}
