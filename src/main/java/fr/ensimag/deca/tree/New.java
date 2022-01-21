package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import java.io.PrintStream;

/**
 * New identifier
 *
 * @author gl60
 * @date 17/01/2022
 */
public class New extends AbstractExpr {
    private AbstractIdentifier className;

    public New(AbstractIdentifier i) {   
        this.className = i;
    }

//TODO
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler){
        RegisterManager regMan = compiler.getRegMan();

        //GPRegister registreDest = regMan.take();
        int nbOfFiled = className.getClassDefinition().getNumberOfFields();
        GPRegister regObject = regMan.take();
        // On alloue le nb d'attribut + 1 place en mémoire :
        compiler.addInstruction(new NEW(nbOfFiled + 1, regObject));
        compiler.addInstruction(new PUSH(regObject));

        // On initialise tous les champs à 0;
        compiler.addInstruction(new BRA(new Label("init." + className.getName())));
        compiler.addInstruction(new SUBSP(1));
        regMan.giveAndPush(regObject);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) {
        throw new UnsupportedOperationException("New is not printable");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        className.decompile(s);
        s.print("()");
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, true);
    }

}

