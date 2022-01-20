package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
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
    protected void verifyFieldVisibility(DecacCompiler compiler,
            AbstractIdentifier superClass, AbstractIdentifier currentClass)
            throws ContextualError {
        Type t = type.verifyType(compiler);
        Validate.isTrue(t.isVoid(), "Cannot have a 'void' type", getLocation());

        ClassDefinition classDef = (ClassDefinition)compiler.getType(currentClass.getName());
        int index = classDef.getSuperClass().getNumberOfFields() + classDef.getNumberOfFields();

        field.setDefinition(new FieldDefinition(t, getLocation(), visibility, classDef, index));

        try{
            classDef.getMembers().declare(field.getName(), field.getFieldDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError(e + " This variable is already defined in this context", getLocation());
        }

        EnvironmentExp superEnvExp = classDef.getSuperClass().getMembers();
        Validate.isTrue(superEnvExp.get(field.getName()).isField(),
                "This expression cannot be defined as a field because its parent already define it another way");

    }

    protected void verifyFieldType(DecacCompiler compiler,
            EnvironmentExp localEnv, AbstractIdentifier currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.println();
        switch(visibility){
            case PUBLIC: //s.print("public ");
            break;
            case PROTECTED: s.print("protected "); 
            break;
        }
        type.decompile(s);
        s.print(" ");
        field.decompile(s);
        initialization.decompile(s);
        s.print(";");
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
