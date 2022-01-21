package fr.ensimag.deca;

import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.syntax.DecaImportParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.nio.file.Paths;
import java.nio.file.Path;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.*;
/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl60
 * @date 01/01/2022
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    /**
     * To write the label name
     */
    private static int numIf = 0;
    private static int numWhile = 0;
    private static int numAnd = 0;
    private static int numOr = 0;
    // to go at the end of the method when the program return and manage the stack
    private static int nbReturn = 0;
    /**
     * To show the div_zero error or not
     */
    private static boolean divideExist = false;
    private static boolean modExist = false;
    private static boolean ioExist = false;
    private static boolean opOvExist = false;
    /**
     * Optimize not operation
     */
    private static boolean isInNotOp = false;
    private static boolean noVoidMethodExist = false;

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.regManager = new RegisterManager(this, compilerOptions.getNbReg());

        try{
            initTypes();
        }catch(ContextualError e){
            System.out.println("a basic type is inizialise 2 times");
        }
    }
    public boolean getNoVoidMethodExist() {
        return noVoidMethodExist;
    }

    public void setNoVoidMethodExist() {
        divideExist = true;
    }


    public boolean getIsInNotOp() {
        return isInNotOp;
    }

    public void inverseIsInNotOp() {
        isInNotOp = !getIsInNotOp();
    }

    public boolean getDivideExist() {
        return divideExist;
    }

    public void setDivideExistTrue() {
        divideExist = true;
    }

    public boolean getModuloExist() {
        return modExist;
    }

    public void setModuloExistTrue() {
        modExist = true;
    }

    public boolean getOpOvExist() {
        return opOvExist;
    }

    public void setOpOvExist() {
        opOvExist = true;
    }

    public boolean getIoExist() {
        return ioExist;
    }

    public void setIoExistTrue() {
        ioExist = true;
    }

    public int getNbReturn() {
        return nbReturn;
    }

    public void incrementNbReturn() {
        nbReturn++;
    }

    public int getNumIf() {
        return numIf;
    }

    public void incrementNumIf() {
        numIf++;
    }

    public int getNumWhile() {
        return numWhile;
    }

    public void incrementNumWhile() {
        numWhile++;
    }

    public int getNumAnd() {
        return numAnd;
    }

    public void incrementNumAnd() {
        numAnd++;
    }

    public int getNumOr() {
        return numOr;
    }

    public void incrementNumOr() {
        numOr++;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * To add TSTO at the beginning of the program
     * @param instruction
     */
    public void addFirst(Instruction instruction) {
        program.addFirst(instruction);
    }

    public void addFirst(Line line) {
        program.addFirst(line);
    }



    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    public RegisterManager getRegMan() {
        return regManager;
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private IMAProgram program = new IMAProgram();


    /**
     * Gestionnaire des registres pour la génération de code
     */
    private RegisterManager regManager;


    public IMAProgram remplaceProgram(IMAProgram newProgram) {
        IMAProgram oldProgram = program;
        program = newProgram;
        return oldProgram;
    }

    /**
     * oldProgram se place au début du programme
     * @param oldProgram
     */
    public void concatenateBeginningProgram(IMAProgram oldProgram) {
        oldProgram.append(program);
        program = oldProgram;
    }

    /**
     * oldProgram se place au début du programme
     * @param oldProgram
     */
    public void concatenateEndProgram(IMAProgram oldProgram) {
        program.append(oldProgram);
    }

//region TREE NODE MAP
    private final Map<Symbol,DeclClass> classNodes = new HashMap<>();

//endregion

//region TYPE ENVIRONMENT

    /**
     * Environnement des types définis lors de l'étape B de la compilation
     */

    private final EnvironmentType typeEnv = new EnvironmentType(null);
    private final SymbolTable typeTable = new SymbolTable();

    public EnvironmentType getTypeEnv(){
        return typeEnv;
    }

    public void initTypes() throws ContextualError{

        List<TypeDefinition> typeDefList = new LinkedList<>();

        typeDefList.add(new TypeDefinition(new IntType(typeTable.create("int")), null));
        typeDefList.add(new TypeDefinition(new FloatType(typeTable.create("float")), null));
        typeDefList.add(new TypeDefinition(new StringType(typeTable.create("string")), null));
        typeDefList.add(new TypeDefinition(new BooleanType(typeTable.create("boolean")), null));
        typeDefList.add(new TypeDefinition(new VoidType(typeTable.create("void")), null));

        ClassType objectType = new ClassType(typeTable.create("Object"), null, null);
        objectType.getDefinition().incNumberOfMethods();
        typeDefList.add(objectType.getDefinition());

        for(TypeDefinition typeDef : typeDefList){
            createType(typeDef.getType().getName(), typeDef);
        }
    }

    public void createType(Symbol symbol, TypeDefinition typeDef) throws ContextualError{
        try {
            //System.out.println("----------------------------------------------------\n");
            //System.out.println(typeEnv.toString() + "\n");
            typeEnv.declare(symbol, typeDef);
        } catch (EnvironmentType.DoubleDefException e) {
            throw new ContextualError(e + " This type is already defined", null);
        }
    }


    //Renvoie null si le type demandé n'est pas trouvé
    public TypeDefinition getType(Symbol type) {
        return typeEnv.get(type);
    }

    public Type getType(String typeName) {
        Symbol type = typeTable.create(typeName);
        return getType(type).getType();
    }
//endregion

//region EXP ENVIRONMENT
    /**
    * correspond à un environement de symboles hors des class et méthodes
    * ne sert que pour le parser en principe, car ensuite les symboles sont tous placé
    * dans les environnements des classes qui leurs correpondent
    **/

    private final EnvironmentExp expEnv = new EnvironmentExp(null);
    private final SymbolTable expTable = new SymbolTable();

    /**
     * Creer l'expression dans l'environnement, ou redéfini sa Définition si celle-ci est à null
     * Sinon renvoie une ContextualError
     * @param exp
     * @param expDef
     * @throws ContextualError
     */
    public void createExp(Symbol exp, ExpDefinition expDef, Location location) throws ContextualError{
        try {
            expEnv.declare(exp, expDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(exp.getName() + " is already defined", location);
        }
    }

    //Renvoie null si l'expression demandé n'est pas trouvé
    public ExpDefinition getExp(Symbol type) {
        return expEnv.get(type);
    }

    public EnvironmentExp getExpEnv() {
        return expEnv;
    }

    public Symbol createSymbol(String expName) {
        return expTable.create(expName);
    }

//endregion




    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() throws ContextualError {
        String sourceFile = source.getAbsolutePath();
        String destFile = null;
        // Done: calculer le nom du fichier .ass à partir du nom du
        // Done: FAIRE: fichier .deca.

        if (!sourceFile.substring(sourceFile.length() - 5, sourceFile.length()).equals(".deca")) {
            throw new ContextualError("Bad extension. Must be '.deca'", null);
        }
        destFile = sourceFile.substring(0, sourceFile.length() - 5) + ".ass";

        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {

        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());

        if(compilerOptions.getDecompile()){
            prog.decompile(out);
            return false;
        }

        prog.verifyProgram(this);
        assert(prog.checkAllDecorations());

        if(compilerOptions.getVerification()){
            return false;
        }
        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }







    public AbstractProgram compileImport(String sourceImport) {
        String sourceFile = sourceImport;
        // On enlève les guillemets
        sourceFile = sourceFile.substring(1, sourceFile.length() - 1);
        String destFile = null;
        // Done: calculer le nom du fichier .ass à partir du nom du
        // Done: FAIRE: fichier .deca.
        // TODO: est-ce qu'il faut vérifier le format du nom en entrée ?

        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Importing file " + sourceFile);
        try {
            return doCompileImport(sourceFile, out, err);
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return null;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return null;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return null;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return null;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private AbstractProgram doCompileImport(String sourceName, PrintStream out, PrintStream err)
            throws DecacFatalError, DecacInternalError {

        AbstractProgram prog = doLexingAndParsingImport(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return null;
        }
        assert(prog.checkAllLocations());

        if(compilerOptions.getDecompile()){
            prog.decompile(out);
            return prog;
        }
        return prog;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsingImport(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        Path filePath = Paths.get(sourceName);
        LOG.debug("fPath: " + filePath);
        if (!filePath.isAbsolute()) {
            Path parentDir = Paths.get(source.getAbsolutePath()).getParent();
            assert(parentDir != null);
            LOG.debug("Parent: " + parentDir);
            filePath = Paths.get(parentDir.toString(), filePath.toString());
            assert(filePath.isAbsolute());
            LOG.debug("New Path: " + filePath);
        }
        try {
            lex = new DecaLexer(CharStreams.fromPath(filePath));
        } catch (IOException ex) {
            throw new RuntimeException("Can't open", ex);
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaImportParser parser = new DecaImportParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }
}
