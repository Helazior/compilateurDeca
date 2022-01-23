package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;

/**
 * String literal
 *
 * @author gl60
 * @date 17/01/2022
 */
public class Selection extends AbstractLValue {
    private AbstractExpr objet;
    private AbstractIdentifier nomDAttribut;

    public Selection(AbstractExpr e, AbstractIdentifier i) {   
        this.objet = e;
        this.nomDAttribut = i;
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) throws DecacFatalError {
        objet.codeGenExpr(compiler);

        // TODO : IMPORTANT !!! METTRE ERREUR DEREF NUL
        // Mais je sais pas si c'est exactement là qu'il faut pour codeGenGetLValue

        this.nomDAttribut.codeGenSelectIdent(compiler,
            ((ClassType) objet.getType()).getDefinition());
    }

    @Override
    protected void codeGenStoreLValue(DecacCompiler compiler) throws DecacFatalError {
        RegisterManager regMan = compiler.getRegMan();
        GPRegister regResultat = regMan.pop();
        regMan.pop(Register.R1);

        // erreur deref nul
        compiler.setIsDerefExistTrue();
        compiler.addInstruction(new CMP(new NullOperand(), Register.R1));
        compiler.addInstruction(new BEQ(new Label("dereferencement_null")));

        regMan.setField(Register.R1, nomDAttribut.getName(),
            ((ClassType) objet.getType()).getDefinition(), regResultat);
        regMan.giveAndPush(regResultat);
    }

    protected void codeGenGetLValue(DecacCompiler compiler) throws DecacFatalError {
        objet.codeGenExpr(compiler);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type exprType = objet.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition typeDef = compiler.getTypeEnv().get(exprType.getName());
        if (!typeDef.isClass()) {
            throw new ContextualError(
                "Type " + exprType.getName() + " is not a class type, and has thus no field.",
                getLocation());
        }
        ClassDefinition classTypeDef = (ClassDefinition) typeDef;
        EnvironmentExp envExp = classTypeDef.getMembers();
        Definition fieldnameDef = nomDAttribut.verifyIdent(compiler, envExp);
        FieldDefinition fieldDef = fieldnameDef.asFieldDefinition(
            "The identifier " + nomDAttribut.getName() +
            " in class " + classTypeDef.getType().getName() + " is not a field", 
            getLocation()
        );
        if (fieldDef.getVisibility() == Visibility.PROTECTED) {
            ClassType exprClassType = exprType.asClassType("Internal error: type is not "+
                "a class type, but should be", getLocation());
            if (!exprClassType.isSubClassOf(currentClass.getType())) {
                throw new ContextualError(
                    "Cannot access " + exprClassType.getName() + "'s attributes from class "
                        + currentClass.getType().getName(),
                    getLocation());
            }
            if (!currentClass.getType().isSubClassOf(fieldDef.getContainingClass().getType())) {
                throw new ContextualError("Cannot access field of class "
                    + fieldDef.getContainingClass().getType()
                    + " from class " + currentClass.getType().getName(), getLocation());
            }
        }
        setType(fieldDef.getType());
        return fieldDef.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        objet.decompile(s);
        s.print(".");
        nomDAttribut.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        objet.iter(f);
        nomDAttribut.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        objet.prettyPrint(s, prefix, false);
        nomDAttribut.prettyPrint(s, prefix, false);
    }
}


