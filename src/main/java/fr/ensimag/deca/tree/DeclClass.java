package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl60
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {


    final private AbstractIdentifier className;
    final private AbstractIdentifier superName;
    final private ListDeclField listDeclField;
    final private ListDeclMethod listDeclMethod;

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superName, ListDeclField listDeclField, ListDeclMethod listDeclMethod) {
        Validate.notNull(className);
        Validate.notNull(superName);
        this.className = className;
        this.superName = superName;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        className.decompile(s);
        s.print(" extends ");
        superName.decompile(s);
        s.println();
        listDeclField.decompile(s);
        s.println();
        s.println();
        listDeclMethod.decompile(s);
        s.unindent();
        s.println("}");
    }


    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        ClassDefinition superDef = (ClassDefinition)compiler.getType(superName.getName());
        ClassType classType = new ClassType(className.getName(), getLocation(), superDef);
        compiler.createType(className.getName(), classType.getDefinition());
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);
        superName.prettyPrint(s, prefix, false);
        listDeclField.prettyPrint(s, prefix, false);
        listDeclMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
        className.iter(f);
        listDeclField.iter(f);
        listDeclMethod.iter(f);
    }

}
