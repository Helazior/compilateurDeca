package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import net.bytebuddy.description.type.TypeDefinition.SuperClassIterator;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl60
 * @date 14/01/2022
 */
public class DeclMethod extends AbstractDeclMethod {
    private AbstractIdentifier returnType;
    private AbstractIdentifier method;
    private ListDeclParam parameters;
    private AbstractMethodBody methodBody;

    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier method, ListDeclParam parameters, AbstractMethodBody methodBody){
        this.returnType = returnType;
        this.method = method;
        this.parameters = parameters;
        this.methodBody = methodBody;
    }

    //TODO

    protected void codeGenDeclMethod(DecacCompiler compiler) {
        RegisterManager regMan = compiler.getRegMan();
        // TODO: récupérer les arguments de la méthode dans la pile
        // On place le label d'erreur à la fin du fichier
        if (!returnType.getType().isFloat()) {
            compiler.setNoVoidMethodExist();
        }

        // ________________________corps du programme___________________________
        methodBody.codeGenMethod(compiler, parameters);
        // goto return
        // Si c'est pas un void et qu'on n'a pas eu de return on va à une erreur
        if (!returnType.getType().isVoid()) {
            compiler.addInstruction(new BRA(new Label ("no_return_error")));
        }
        compiler.addLabel(new Label("return" + compiler.getNbReturn()));

        //________________________
        // On revient placer ce qu'il manque avec les infos du prog
        // Début de la méthode = label du nom de la méthode
        compiler.addFirst(new Line(new Label("bodyMethod." + getClass().getName() + "." + method.getName())));
        // On empile tous les registres qu'on veut utiliser au début de la méthode et on les restaure à la fin
        regMan.restoreRegisters();

        // TODO: récup le nom :
        compiler.addFirst(new Line(new Label("bodyMethod.class.method")));
        // On empile tous les registres qu'on veut utiliser et on les restaure à la fin
        regMan.restoreRegisters();

        // goto erreur return en cas de non return
        // On return
        compiler.addInstruction(new RTS());
    }


    @Override
    public void decompile(IndentPrintStream s) {
        returnType.decompile(s);
        s.print(" ");
        method.decompile(s);
        s.print(" (");
        parameters.decompile(s);
        s.print(") ");
        methodBody.decompile(s);
    }


    @Override
    protected void verifyMethodSignature(DecacCompiler compiler, ClassDefinition superClass,
            ClassDefinition currentClass) throws ContextualError {
        Type t = returnType.verifyType(compiler);
        Symbol methodName = method.getName();
        Signature sig = parameters.verifyListParamSignature(compiler);

        int index = superClass.getNumberOfMethods() + currentClass.getNumberOfMethods();

        ExpDefinition superExp = superClass.getMembers().get(methodName);
        if(superExp != null){
            if(!superExp.isMethod()){
                throw new ContextualError("This expression cannot be defined as a method " +
                "because its parent already define it another way", getLocation());
            }

            MethodDefinition superMethod = (MethodDefinition)superExp;
            index = superMethod.getIndex();

            if(!t.isSubTypeOf(superMethod.getType())){
                throw new ContextualError("the returned type of this method should be " +
                "a sub-type of the one of its parent ", getLocation());
            }
            if(!sig.equals(superMethod.getSignature())){
                throw new ContextualError("The method signature doesn't match its parent", getLocation());
            }

        }

        method.setDefinition(new MethodDefinition(t, getLocation(), sig, index));

        try{
            currentClass.getMembers().declare(methodName, method.getMethodDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError("This varibale is already defined in this context", getLocation());
        }
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler,
            AbstractIdentifier currentClass) throws ContextualError {
        Type ret = returnType.verifyType(compiler);

        ClassDefinition classDef = (ClassDefinition)compiler.getType(currentClass.getName());
        EnvironmentExp methodEnv = new EnvironmentExp(classDef.getMembers());
        parameters.verifyListParamType(compiler, methodEnv);
        methodBody.verifyMethodBody(compiler, methodEnv, currentClass, ret);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnType.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        parameters.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnType.iter(f);
        method.iter(f);
        parameters.iter(f);
        method.iter(f);
    }

    @Override
    public AbstractIdentifier getName() {
        return this.method;
    }

}
