package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

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

    public DeclClass(AbstractIdentifier currentClass, AbstractIdentifier superClass, ListDeclField listDeclField, ListDeclMethod listDeclMethod) {
        Validate.notNull(currentClass);
        Validate.notNull(superClass);
        this.currentClass = currentClass;
        this.superClass = superClass;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        currentClass.decompile(s);
        superClass.decompile(s);
        listDeclField.decompile(s);
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

}
