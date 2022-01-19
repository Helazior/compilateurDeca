package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

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
    final private Visibility visibility;

    public DeclField(AbstractIdentifier type, AbstractIdentifier field, AbstractInitialization initialization, Visibility visibility) {
        Validate.notNull(type);
        Validate.notNull(field);
        Validate.notNull(initialization);
        Validate.notNull(visibility);
        this.type = type;
        this.field = field;
        this.initialization = initialization;
        this.visibility = visibility;
    }

    //TODO toute la suite
    @Override
    protected FieldDefinition verifyFieldVisibility(DecacCompiler compiler,
            AbstractIdentifier superClass, AbstractIdentifier currentClass)
            throws ContextualError {
        Type t = type.verifyType(compiler);
        if( t.isVoid()){
            throw new ContextualError("Cannot have a 'void' type", getLocation());
        }

        ClassDefinition currDef = (ClassDefinition)compiler.getType(currentClass.getName());
        field.setDefinition(new FieldDefinition(t, getLocation(), visibility, currDef, 0));
        return field.getFieldDefinition();
    }

    protected void verifyFieldType(DecacCompiler compiler,
            EnvironmentExp localEnv, AbstractIdentifier currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.println(" ");
        field.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        field.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
