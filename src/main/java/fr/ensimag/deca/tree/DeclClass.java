package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;
import fr.ensimag.deca.context.*;

import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl60
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {


    final private AbstractIdentifier currentClass;
    final private AbstractIdentifier superClass;
    final private ListDeclField listDeclField;
    final private ListDeclMethod listDeclMethod;

    public DeclClass(AbstractIdentifier currentClass, AbstractIdentifier superClass,
                     ListDeclField listDeclField, ListDeclMethod listDeclMethod) {
        Validate.notNull(currentClass);
        Validate.notNull(superClass);
        this.currentClass = currentClass;
        this.superClass = superClass;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;
    }

    @Override
    protected void codeGenClass(DecacCompiler compiler) {
        listDeclMethod.codeGenListDeclMethod(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        s.print("class");
        currentClass.decompile(s);
        s.print(" extends ");
        superClass.decompile(s);
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
        Symbol name = currentClass.getName();
        Symbol superName = superClass.getName();

        ClassDefinition superDef = (ClassDefinition)compiler.getType(superName);
        ClassType classType = new ClassType(name, getLocation(), superDef);
        compiler.createType(currentClass.getName(), classType.getDefinition());

        Validate.isTrue(compiler.getType(superName).isClass());
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        listDeclField.verifyListFieldVisibility(compiler, superClass, currentClass);
        listDeclMethod.verifyListMethodSignature(compiler, superClass, currentClass);
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        currentClass.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        listDeclField.prettyPrint(s, prefix, false);
        listDeclMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        currentClass.iter(f);
        currentClass.iter(f);
        listDeclField.iter(f);
        listDeclMethod.iter(f);
    }

    @Override
    public int codeGenDeclMethod(DecacCompiler compiler, IMAProgram program)
            throws ContextualError {
        RegisterManager regman = compiler.getRegMan();
        Symbol cnSymbol = className.getName();
        ClassDefinition type = (ClassDefinition) compiler.getTypeEnv().get(cnSymbol);
        EnvironmentExp expEnv = type.getMembers();
        int size = type.getNumberOfMethods();

        // We define an asm function building the classtable
        program.addLabel(new Label("classTableInit." + cnSymbol));
        // We call the parent's classtable builder
        program.addInstruction(new BSR(
            new LabelOperand(new Label("classTableInit." + superName.getName()))
        ));
        // We put a pointer to the parent class
        program.addInstruction(new LOAD(
            new LabelOperand(new Label("classTableInit." + superName.getName())),
            Register.R1));
        program.addInstruction(new STORE(
            Register.R1,
            new RegisterOffset(0, Register.SP)));
        // We (re)define the new methods
        for (AbstractDeclMethod meth : listDeclMethod.getList()) {
            Symbol methName = meth.getName().getName();
            int index = expEnv.get(methName).asMethodDefinition(
                "Internal Error: "+methName+" in class "+cnSymbol+" is not a method",
                getLocation()
            ).getIndex();
            program.addInstruction(new LOAD(
                new LabelOperand(new Label("methodBody."+cnSymbol+"."+methName)),
                Register.R1));
            program.addInstruction(new STORE(
                Register.R1,
                new RegisterOffset(index, Register.SP)));
        }
        // The asm function building the classtable is finished
        program.addInstruction(new RTS());
        return type.getNumberOfMethods();
    }
}
