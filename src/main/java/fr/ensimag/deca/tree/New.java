package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BSR;
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

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //Type t = compiler.getType(className.getName()).getType();
        Type t = className.verifyType(compiler);
        if(!t.isClass()){
            throw new ContextualError("The 'New' symbol can only be use to defined Classes", getLocation());
        }
        setType(t);
        return t;
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler){
        RegisterManager regMan = compiler.getRegMan();

        compiler.addComment("New");
        //GPRegister registreDest = regMan.take();
        int nbOfFiled = ((ClassType) className.getType()).getDefinition().getNumberOfFields();
        GPRegister regObject = regMan.take();
        // On alloue le nb d'attribut + 1 place en mémoire :
        compiler.addInstruction(new NEW(nbOfFiled + 1, regObject));
        compiler.addInstruction(new PUSH(regObject));

        // On initialise tous les champs à 0;
        compiler.addInstruction(new BSR(new Label("init." + className.getName())));
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

