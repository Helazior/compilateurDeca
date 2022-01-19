package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.jar.Attributes.Name;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl60
 * @date 14/01/2022
 */
public class DeclMethod extends AbstractDeclMethod {
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListDeclParam parameters;
    private AbstractMethodBody method;

    public DeclMethod(AbstractIdentifier typeReturn, AbstractIdentifier name, ListDeclParam parameters, AbstractMethodBody methodBody){
        this.type = typeReturn;
        this.name = name;
        this.parameters = parameters;
        this.method = methodBody;
    }

    //TODO
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        type.decompile(s);
        name.decompile(s);
        parameters.decompile(s);
        method.decompile(s);
        s.unindent();
        s.println("}");    }


    @Override
    protected void verifyMethodSignature(DecacCompiler compiler, ClassDefinition superClass) 
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        parameters.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        parameters.iter(f);
        method.iter(f);
    }

}
