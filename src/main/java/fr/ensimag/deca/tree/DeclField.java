package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
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
    protected Symbol getName() {
        return field.getName();
    }

    @Override
    protected Type getType() {
        return type.getType();
    }

    @Override
    protected AbstractInitialization getInitialization() {
        return initialization;
    }

    @Override
    protected void verifyFieldVisibility(DecacCompiler compiler,
            ClassDefinition superClass, ClassDefinition currentClass)
            throws ContextualError {
        Type t = compiler.getType(type.getName()).getType();
        if(t.isVoid()){
            throw new ContextualError("A field cannot be a 'void' type", getLocation());
        }

        ExpDefinition superExp = superClass.getMembers().get(field.getName());
        if(superExp != null && !superExp.isField()){
            throw new ContextualError("This expression cannot be defined as a field " +
                "because its parent already define it another way", getLocation());
        }

        int index = currentClass.getNumberOfFields() + superClass.getNumberOfFields();
        field.setDefinition(new FieldDefinition(t, getLocation(), visibility, currentClass, index));

        try{
            currentClass.getMembers().declare(field.getName(), field.getFieldDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError("This variable is already defined in this context", getLocation());
        }
    }

    protected void verifyFieldType(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
        Type t = type.verifyType(compiler);
        initialization.verifyInitialization(compiler, t, currentClass.getMembers(), currentClass);
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
