package fr.ensimag.deca;

import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
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
import java.lang.instrument.ClassDefinition;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.List;

import fr.ensimag.deca.codegen.RegisterManager;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.EnvironmentExp;

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
    /**
     * To show the div_zero error or not
     */
    private static boolean divideExist = false;
    private static boolean ioExist = false;

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

    public boolean getDivideExist() {
        return divideExist;
    }

    public void setDivideExistTrue() {
        divideExist = true;
    }

    public boolean getIoExist() {
        return ioExist;
    }

    public void setIoExistTrue() {
        ioExist = true;
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
    private final IMAProgram program = new IMAProgram();


    /**
     * (demander à Gwennan en cas de PB)
     */
    private RegisterManager regManager;

    /**
     * Permet d'avoir des types dans la partie B
     * (demander à Gwennan en cas de PB)
     * 
     * Cela correspond en fait à la definition de
     * l'environnmeent des types de base du compilateur
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

        for(TypeDefinition typeDef : typeDefList){
            createType(typeDef.getType().getName(), typeDef);
        }
    }

    private void createType(Symbol symbol, TypeDefinition typeDef) throws ContextualError{
        try {
            typeEnv.declare(symbol, typeDef);
        } catch (EnvironmentType.DoubleDefException e) {
            throw new ContextualError(e + " This type is already defined", null);
        }
    }



    public Type getType(Symbol type, Location location) throws ContextualError {
        TypeDefinition typeDef = typeEnv.get(type);
        if(typeDef == null){
            throw new ContextualError(type.getName() + " is not recognise as a type", location);
        }
        return typeDef.getType();
    }

    public Type getType(String typeName) throws ContextualError{
        Symbol type = typeTable.create(typeName);
        return getType(type, null);
    }




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

    public Symbol createSymbol(String expName) {
        return expTable.create(expName);
    }






    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = null;
        // Done: calculer le nom du fichier .ass à partir du nom du
        // Done: FAIRE: fichier .deca.
        // TODO: est-ce qu'il faut vérifier le format du nom en entrée ?

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

}
