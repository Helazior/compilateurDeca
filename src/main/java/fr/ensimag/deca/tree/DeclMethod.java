package fr.ensimag.deca.tree;

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
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        returnType.decompile(s);
        method.decompile(s);
        parameters.decompile(s);
        methodBody.decompile(s);
        s.unindent();
        s.println("}");    }


    @Override
    protected void verifyMethodSignature(DecacCompiler compiler, AbstractIdentifier superClass,
            AbstractIdentifier currentClass) throws ContextualError {
        Type t = returnType.verifyType(compiler);
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

}
