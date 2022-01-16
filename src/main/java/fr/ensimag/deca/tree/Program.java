package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.deca.codegen.codeGenError;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl60
 * @date 01/01/2022
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclImport imports, ListDeclClass classes, AbstractMain main) {
        Validate.notNull(imports);
        Validate.notNull(classes);
        Validate.notNull(main);
        this.imports = imports;
        this.classes = classes;
        this.main = main;
    }
    public ListDeclImport getimports(){
        return imports;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclImport imports;
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        //TODO: les 3 passes
        classes.verifyListClass(compiler);

        //classes.verifyListClassMembers(compiler);

        //classes.verifyListClassBody(compiler);

        //TODO: check classes

        main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }


    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // liste des déclarations de variables
        // Les adresses des variables globales sont de la forme
        // 1(GB), 2(GB), 3(GB).... Associer une adresse à chaque variable consiste à modifier le champ `operand`
        // de sa définition via la méthode Definition.setOperand() : voir les classes VariableDefinition et ExpDefinition
        // Récupéré avec getOperand


        compiler.addComment("Main program");
        // parcours de l'arbre. On écrit dans le main :
        main.codeGenMain(compiler);

        // termine le programme
        compiler.addInstruction(new HALT());

        if (compiler.getDivideExist()) {
            codeGenError.divByZeroError(compiler);
        }
        if (compiler.getModuloExist()) {
            codeGenError.modByZeroError(compiler);
        }
        if (compiler.getIoExist()) {
            codeGenError.ioError(compiler);
        }
        if (compiler.getOpOvExist()) {
            codeGenError.overflowError(compiler);
        }

        codeGenError.stackOverflowError(compiler);

        assert(compiler.getRegMan().isStackEmpty());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}   
