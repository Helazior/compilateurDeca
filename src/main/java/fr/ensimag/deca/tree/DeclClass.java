package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
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
        s.print("class ");
        currentClass.decompile(s);
        s.print(" extends ");
        superClass.decompile(s);
        s.println(" {");
        s.indent();
        listDeclField.decompile(s);
        s.println("\n");
        listDeclMethod.decompile(s);
        s.unindent();
        s.println("}");
    }


    /**
     * Astuce ou idée pour l'extension :
     * On effectue un appel récursif sur le parent de la classe actuel de sorte que
     * la première class verfié soit la racine de la chaine de classe et que la classe
     * actuel soit appelé une fois que toutes ses classes parentes ont bien été défini.
     * De cette manière on garanti l'ordre de parcours de l'arbre
     *
     * Condition d'arret : Si la class actuel n'a plus de parent, ou si elle a déja été traité
     *
     * Problème : On n'a aucune référence vers le noeud de l'arbre correspondant
     * à la définition de la classe parent
     *
     * Intuition : on peut faire la construction de ces réfrence dans l'environnmenet du compilateur
     * dans une première passe, ou éventuellement rajouter des arguments dans les définitions de classes
     */


    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError{
        Symbol name = currentClass.getName();
        Symbol superName = superClass.getName();

        TypeDefinition def = compiler.getType(superName);

        if(def != null && !def.isClass()){
            throw new ContextualError("the parent of this class should be a class", getLocation());
        }
        ClassType classType = new ClassType(name, getLocation(), (ClassDefinition)def);

        currentClass.setDefinition(classType.getDefinition());
        try {
            compiler.createType(currentClass.getName(), classType.getDefinition());
        } catch (ContextualError e) {
            throw new ContextualError("This class is already defined in this context", getLocation());
        }
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        ClassDefinition classDef = (ClassDefinition)compiler.getType(currentClass.getName());
        ClassDefinition superClassDef = (ClassDefinition)compiler.getType(superClass.getName());

        listDeclField.verifyListFieldVisibility(compiler, superClassDef, classDef);
        listDeclMethod.verifyListMethodSignature(compiler, superClassDef, classDef);
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        listDeclField.verifyListFieldType(compiler, currentClass);
        listDeclMethod.verifyListMethodBody(compiler, currentClass);
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
        Symbol className = currentClass.getName();
        ClassDefinition type = (ClassDefinition) compiler.getTypeEnv().get(className);
        EnvironmentExp expEnv = type.getMembers();
        int size = type.getNumberOfMethods();

        // We define an asm function building the classtable
        program.addLabel(new Label("classTableInit." + className));
        // We call the parent's classtable builder
        program.addInstruction(new BSR(
            new LabelOperand(new Label("classTableInit." + superClass.getName()))
        ));
        // We put a pointer to the parent class
        program.addInstruction(new LOAD(
            new LabelOperand(new Label("classTableInit." + superClass.getName())),
            Register.R1));
        program.addInstruction(new STORE(
            Register.R1,
            new RegisterOffset(0, Register.SP)));
        // We (re)define the new methods
        for (AbstractDeclMethod meth : listDeclMethod.getList()) {
            Symbol methName = meth.getName().getName();
            int index = expEnv.get(methName).asMethodDefinition(
                "Internal Error: "+methName+" in class "+className+" is not a method",
                getLocation()
            ).getIndex();
            program.addInstruction(new LOAD(
                new LabelOperand(new Label("methodBody."+className+"."+methName)),
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
