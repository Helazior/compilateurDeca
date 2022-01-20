package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.jar.Attributes.Name;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl60
 * @date 14/01/2022
 */
public class DeclParam extends AbstractDeclParam {
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    
    public DeclParam(AbstractIdentifier type, AbstractIdentifier name){
        this.name = name;
        this.type = type;
    }

    //TODO
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        name.decompile(s);
        type.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected Type verifyParamSignature(DecacCompiler compiler)
            throws ContextualError {
        Type t = type.verifyType(compiler);
        if(t.isVoid()){
            throw new ContextualError("Cannot pass a voidType object in argument", getLocation());
        }
        return t;
    }

    @Override
    protected void verifyParamType(DecacCompiler compiler, EnvironmentExp methodEnv) throws ContextualError {
        try{
            methodEnv.declare(name.getName(), new ParamDefinition(type.getType(), getLocation()));
        } catch(DoubleDefException e){
            throw new ContextualError("A parameter with the same name already exist in this context", getLocation());
        }
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        type.iter(f);    
    }

}
