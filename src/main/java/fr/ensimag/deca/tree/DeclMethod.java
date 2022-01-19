package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;
import java.util.List;
import java.util.jar.Attributes.Name;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl60
 * @date 14/01/2022
 */
public class DeclMethod extends AbstractDeclMethod {
    private AbstractIdentifier type;
    private AbstractIdentifier method;
    private ListDeclParam parameters;
    private AbstractMethodBody methodBody;

    public DeclMethod(AbstractIdentifier typeReturn, AbstractIdentifier method, ListDeclParam parameters, AbstractMethodBody methodBody){
        this.type = typeReturn;
        this.method = method;
        this.parameters = parameters;
        this.methodBody = methodBody;
    }

    //TODO

    protected void codeGenDeclMethod(DecacCompiler compiler) {
        RegisterManager regMan = compiler.getRegMan();
        // TODO: récupérer les arguments de la méthode dans la pile
        if (!type.getType().isFloat()) {
            compiler.setNoVoidMethodExist();
        }

        // ________________________corps du programme___________________________
        methodBody.codeGenMethod(compiler);
        // goto return
        // Si c'est pas un void et qu'on n'a pas eu de return on va à une erreur
        if (!type.getType().isVoid()) {
            compiler.addInstruction(new BRA(new Label ("no_return_error")));
        }
        compiler.addLabel(new Label("return" + compiler.getNbReturn()));


        //________________________
        // On revient placer ce qu'il manque avec les infos du prog
        // Début de la méthode = label du nom de la méthode

        // TODO: récup le nom de la classe :
        compiler.addFirst(new Line(new Label("bodyMethod." + getClass().getName() + "." + method.getName())));
        // On empile tous les registres qu'on veut utiliser et on les restaure à la fin
        regMan.restoreRegisters();

        // goto erreur return en cas de non return
        // On return
        compiler.addInstruction(new RTS());
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        type.decompile(s);
        s.print(" ");
        method.decompile(s);
        s.print(" (");
        parameters.decompile(s);
        s.print(") ");
        methodBody.decompile(s);
        s.unindent();
        s.println("}");    
    }


    @Override
    protected void verifyMethodSignature(DecacCompiler compiler, AbstractIdentifier superClass,
            AbstractIdentifier currentClass) throws ContextualError {
        Type t = type.verifyType(compiler);
        Symbol methodName = method.getName();
        Signature sig = parameters.verifyListParamSignature(compiler);

        ClassDefinition classDef = (ClassDefinition)compiler.getType(currentClass.getName());
        EnvironmentExp superEnvExp = classDef.getSuperClass().getMembers();

        if(superEnvExp.get(methodName) != null){
            MethodDefinition superMethod = (MethodDefinition)superEnvExp.get(methodName);

            int index = superMethod.getIndex();
            method.setDefinition(new MethodDefinition(t, getLocation(), sig, index));

            Validate.isTrue(sig == superMethod.getSignature(),
                    "The method signature doesn't match its parent");
            Validate.isTrue(t.isSubTypeOf(superMethod.getType()),
                    "the returned type of this method should be a sub-type of the one of its parent ");
        } else {
            int index = classDef.getSuperClass().getNumberOfFields() + classDef.getNumberOfFields();
            method.setDefinition(new MethodDefinition(t, getLocation(), sig, index));
        }

        try{
            classDef.getMembers().declare(methodName, method.getMethodDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError(e + " This varibale is already defined in this context", getLocation());
        }
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler,
            EnvironmentExp localEnv, AbstractIdentifier currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        parameters.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        method.iter(f);
        parameters.iter(f);
        method.iter(f);
    }

}
