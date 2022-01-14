package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.jar.Attributes.Name;

import org.apache.commons.lang.Validate;

/**
 * @author gl60
 * @date 14/01/2022
 */
public class DeclField extends AbstractDeclField {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier field;
    final private AbstractInitialization initialization;

    public DeclField(AbstractIdentifier type, AbstractIdentifier field, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(field);
        Validate.notNull(initialization);
        this.type = type;
        this.field = field;
        this.initialization = initialization;
    }

    //TODO toute la suite
    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.println(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
