package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
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

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    public Program(ListDeclImport imports, ListDeclClass classes, AbstractMain main) {
        Validate.notNull(imports);
        Validate.notNull(classes);
        Validate.notNull(main);
        this.imports = imports;
        this.classes = classes;
        this.main = main;
    }

    public ListDeclImport getImports() {
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

        loadNodes(compiler);

        ListDeclClass allClasses = compiler.getListClassNodes();
        allClasses.verifyListClass(compiler);
        allClasses.verifyListClassMembers(compiler);
        //classes.verifyListClass(compiler);
        //classes.verifyListClassMembers(compiler);

        classes.verifyListClassBody(compiler);

        main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    private void errorsMessages(DecacCompiler compiler) {
        compiler.addComment("---------------------------------------------------");
        compiler.addComment("               Messages d'erreur");
        compiler.addComment("---------------------------------------------------");
        if (compiler.getIoExist()) {
            codeGenError.ioError(compiler);
        }

        if (compiler.getNoVoidMethodExist()) {
            codeGenError.noReturnError(compiler);
        }

        if (!compiler.getCompilerOptions().getNoCheck()) {
            if (compiler.getDivideExist()) {
                codeGenError.divByZeroError(compiler);
            }
            if (compiler.getModuloExist()) {
                codeGenError.modByZeroError(compiler);
            }

            if (compiler.getOpOvExist()) {
                codeGenError.overflowError(compiler);
            }

            codeGenError.stackOverflowError(compiler);
        }
    }
    
    @Override
    public void loadNodes(DecacCompiler compiler) throws ContextualError {
        if(imports != null){
            imports.loadListImportNodes(compiler);
        }
        classes.loadListClassNodes(compiler);
    }


    @Override
    public void codeGenProgram(DecacCompiler compiler) throws DecacFatalError {
        // liste des déclarations de variables
        // Les adresses des variables globales sont de la forme
        // 1(GB), 2(GB), 3(GB).... Associer une adresse à chaque variable consiste à modifier le champ `operand`
        // de sa définition via la méthode Definition.setOperand() : voir les classes VariableDefinition et ExpDefinition
        // Récupéré avec getOperand

        RegisterManager regMan = compiler.getRegMan();
        int tablesize = regMan.declareClasses(classes);
        IMAProgram classtableGen = compiler.remplaceProgram(new IMAProgram());
        
        classes.codeGenListClass(compiler);
        IMAProgram classesBodies = compiler.remplaceProgram(new IMAProgram());

        // parcours de l'arbre. On écrit dans le main :
        main.codeGenMain(compiler, tablesize);
        compiler.addFirst(new Line("Main program"));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addFirst(new BOV(new Label("stack_overflow_error")));
            compiler.addFirst(new TSTO(compiler.getRegMan().getMaxSizeStack()));
        }
        assert(compiler.getRegMan().isStackEmpty());
        // termine le programme
        compiler.addInstruction(new HALT());


        IMAProgram mainProg = compiler.remplaceProgram(new IMAProgram());

        errorsMessages(compiler);

        IMAProgram errorsFns = compiler.remplaceProgram(new IMAProgram());

        compiler.concatenateBeginningProgram(mainProg);
        compiler.concatenateBeginningProgram(classtableGen);

        compiler.concatenateEndProgram(classesBodies);
        compiler.concatenateEndProgram(errorsFns);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (imports != null){
            getImports().decompile(s);
        }
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
        if (imports != null) {
            imports.prettyPrint(s, prefix, false);
        }
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}   
