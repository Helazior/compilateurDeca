package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
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


    private void initAttributs(DecacCompiler compiler) throws ContextualError {
        // TODO: itérer sur les parents si extend !
        RegisterManager regMan = compiler.getRegMan();
        compiler.addComment("; ---------- Initialisation des champs de " + getClass().getName());
        compiler.addLabel(new Label("init." + getClass().getName()));
        for (AbstractDeclField declField : listDeclField.getList()) {
            // On déclare chaque attribut :
            if (declField.getInitialization().isInitialized()) {
                // initialisé
                declField.getInitialization().pushValue(compiler);
                regMan.pop(Register.R0);
            } else {
                // valeur par défaut
                if (declField.getType().isInt()) {
                    compiler.addInstruction(new LOAD(0, Register.R0));
                } else if (declField.getType().isFloat()) {
                    compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R0));
                } else {
                    compiler.addInstruction(new LOAD(null, Register.R0));
                }
            }

            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
            compiler.getRegMan().setField(Register.R1, declField.getName(), declField.getType(), Register.R0, getLocation());
        }

        // On revient au New
        compiler.addInstruction(new RTS());
    }

    @Override
    protected void codeGenClass(DecacCompiler compiler) throws ContextualError {
        compiler.addComment("--------------------------------------------------");
        compiler.addComment("                  Classe " + currentClass.getName());
        compiler.addComment("--------------------------------------------------");
        // On initialise tous les attributs :
        initAttributs(compiler);
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

    public int lastPassNumber; //PABO
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
     * Intuition : on peut faire la construction de ces réfrences dans l'environnmenet du compilateur
     * dans une première passe, ou éventuellement rajouter des arguments dans les définitions de classes
     */
    @Override
    protected void loadClassNodes(DecacCompiler compiler) throws ContextualError{
        lastPassNumber = 0;
        try {
            compiler.addClassNode(currentClass.getName(), this);
        } catch (EnvironmentType.DoubleDefException e) {
            throw new ContextualError("This class is already defined in this context", getLocation());
        }
    }


    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError{
        Symbol name = currentClass.getName();
        Symbol superName = superClass.getName();

        //region Recusion Part

        /** Condition d'arret :
         * > si la classe est déja défini on s'arrete, car cela signifie
         * > que toutes les classes supérieur on aussi été défini
         */
        if(compiler.getType(name) != null){
            return;
        }

        if(lastPassNumber == 1){
            // Ici, on rencontre une classe qui n'est pas défini mais qui a déja été appelé par cette fonction avant
            throw new ContextualError("It seams that there is a fishy loop in the class hierarchy", getLocation());
        }
        lastPassNumber = 1;

        //récursion
        if(superName.getName() != "Object"){
            DeclClass superClassNode = compiler.getClassNode(superName);
            if(superClassNode == null){
                throw new ContextualError("the parent of this class should be a class", getLocation());
            }
            superClassNode.verifyClass(compiler);
        }

        //endregion

        TypeDefinition def = compiler.getType(superName);

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
        Symbol name = currentClass.getName();
        Symbol superName = superClass.getName();

        ClassDefinition classDef = (ClassDefinition)compiler.getType(name);
        ClassDefinition superClassDef = (ClassDefinition)compiler.getType(superName);

        //region Recusion Part

        /** Condition d'arret :
         * > si la classe à déja été parcouru par cette fonction on s'arrete
         * > On parcours ainsi tout le monde car on a garanti à la passe précedente qu'il n'y avait pas de boucle
         */
        if(lastPassNumber == 2 || name.getName().equals("Object")){
            return;
        }
        lastPassNumber = 2;

        //récursion
        if(superName.getName() != "Object"){
            compiler.getClassNode(superName).verifyClassMembers(compiler);
        }

        //endregion

        listDeclField.verifyListFieldVisibility(compiler, superClassDef, classDef);
        listDeclMethod.verifyListMethodSignature(compiler, superClassDef, classDef);
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        ClassDefinition classDef = (ClassDefinition)compiler.getType(currentClass.getName());

        listDeclField.verifyListFieldType(compiler, classDef);
        listDeclMethod.verifyListMethodBody(compiler, classDef);
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

    /** Adds at the end of program the code to populate the classtable.
     * SP should be at the first free memorycell where the classtble can
     * be written.
     * Uses the super's classtable builder, and adds the newly defined classes
     */
    @Override
    public int codeGenClassTableFn(DecacCompiler compiler, IMAProgram program, int stackPos) {
        RegisterManager regman = compiler.getRegMan();
        Symbol className = currentClass.getName();
        ClassDefinition type = (ClassDefinition) compiler.getTypeEnv().get(className);
        type.setTablePlace(stackPos);
        EnvironmentExp expEnv = type.getMembers();
        int size = type.getNumberOfMethods() + 1;

        program.addComment("----------------------------------------");
        program.addComment(className.toString());
        // We define an asm function building the classtable
        program.addLabel(new Label("classTableInit." + className));
        // We call the parent's classtable builder
        program.addInstruction(new BSR(
            new LabelOperand(new Label("classTableInit." + superClass.getName()))
        ));

        for (AbstractDeclMethod meth : listDeclMethod.getList()) {
            Symbol methName = meth.getName().getName();
            int index;
            try {
                index = expEnv.get(methName).asMethodDefinition(
                    "Internal Error: "+methName+" in class "+className+" is not a method",
                    getLocation()
                ).getIndex();
            } catch (ContextualError e){
                throw new RuntimeException(e.toString());
            }
            program.addInstruction(new LOAD(
                new LabelOperand(new Label("methodBody."+className+"."+methName)),
                Register.R1));
            program.addInstruction(new STORE(
                Register.R1,
                new RegisterOffset(index + stackPos, Register.GB)));
        }
        // The asm function building the classtable is finished
        program.addInstruction(new RTS());
        return size;
    }

    /** Sets the pointer to the parent in the method table */
    @Override
    public void codeGenClassTableMain(DecacCompiler compiler, IMAProgram program) {
        RegisterManager regman = compiler.getRegMan();
        ClassDefinition type = (ClassDefinition) compiler.getTypeEnv().get(currentClass.getName());
        ClassDefinition superType = (ClassDefinition) compiler.getTypeEnv().get(superClass.getName());
        int offset = type.getTablePlace();
        int superOffset = superType.getTablePlace();

        program.addInstruction(new BSR(
            new LabelOperand(new Label("classTableInit." + type.getType().getName()))
        ));
        program.addInstruction(new LOAD(
            new RegisterOffset(superOffset, Register.GB), Register.R1));
        program.addInstruction(new STORE(
            Register.R1, new RegisterOffset(offset, Register.GB)));
    }
}
