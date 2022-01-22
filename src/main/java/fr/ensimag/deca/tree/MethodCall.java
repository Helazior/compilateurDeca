package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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

//TODO
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
    protected void codeGenExpr(DecacCompiler compiler){
        RegisterManager regMan = compiler.getRegMan();
        // on push tous les registres dans la pile pour qu'ils ne gênent pas pendant le calcul d'argument
        for (AbstractExpr field : parametres.getList()) {
            field.codeGenExpr(compiler);
        }
        regMan.popInStack(parametres.getList().size());

        // On saute au label de la méthode
        compiler.addInstruction(new BRA(new Label("bodyMethod." + getClass().getName() + "." + nomDeMethode)));
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, Boolean printHex) {
        throw new UnsupportedOperationException("Method is not printable");
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

