package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

/**
 * String literal
 *
 * @author gl60
 * @date 17/01/2022
 */
public class MethodCall extends AbstractExpr {
    private AbstractExpr objet;
    private AbstractIdentifier nomDeMethode;
    private ListExpr parametres;

    public MethodCall(AbstractExpr e, AbstractIdentifier i, ListExpr p) {   
        this.objet = e;
        this.nomDeMethode = i;
        this.parametres = p;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type exprType = objet.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition typeDef = compiler.getTypeEnv().get(exprType.getName());
        if (!typeDef.isClass()) {
            throw new ContextualError(
                "Type " + exprType.getName() + " is not a class type, and has thus no method.",
                getLocation());
        }
        ClassDefinition classTypeDef = (ClassDefinition) typeDef;
        EnvironmentExp envExp = classTypeDef.getMembers();
        
        Definition methodnameDef = nomDeMethode.verifyIdent(compiler, envExp);
        MethodDefinition methodDef = methodnameDef.asMethodDefinition(
            "The identifier " + nomDeMethode.getName() +
            " in class " + classTypeDef.getType().getName() + " is not a method", 
            getLocation()
        );

        parametres.verifyParams(compiler, localEnv, currentClass, methodDef.getSignature());
        setType(methodDef.getType());
        return methodDef.getType();
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) throws DecacFatalError {
        RegisterManager regMan = compiler.getRegMan();
        objet.codeGenExpr(compiler);
        for (AbstractExpr field : parametres.getList()) {
            field.codeGenExpr(compiler);
        }
        // Il y a this en plus
        regMan.prepareMethodCall(parametres.size() + 1);

        // On saute à l'adresse de la méthode
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), Register.R1));
        // erreur deref nul
        compiler.setIsDerefExistTrue();
        compiler.addInstruction(new CMP(new NullOperand(), Register.R1));
        compiler.addInstruction(new BEQ(new Label("dereferencement..null")));

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R1), Register.R1));
        MethodDefinition methodDef = (MethodDefinition) nomDeMethode.getDefinition();
        compiler.addInstruction(new BSR(new RegisterOffset(methodDef.getIndex() + 1, Register.R1)));

        // On remet la stack comme avant l'appel de méthode
        compiler.addInstruction(new SUBSP(parametres.size() + 2)); // + this * 2: in the bottom and the top
        regMan.push(Register.R1);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        objet.decompile(s);
        s.print(".");
        nomDeMethode.decompile(s);
        s.print("(");
        parametres.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        objet.iter(f);
        nomDeMethode.iter(f);
        parametres.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        objet.prettyPrint(s, prefix, false);
        nomDeMethode.prettyPrint(s, prefix, false);
        parametres.prettyPrint(s, prefix, true);
    }

}

