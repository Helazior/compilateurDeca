package fr.ensimag.deca.codegen;

import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

public class RegisterManager {
    private static final Logger LOG = Logger.getLogger(RegisterManager.class);
    // List of state of the registers. 
    // -1: externally used.
    //  0: Not used.
    // > 0: used for the stacked variable of this index
    private int[] registers;
    private boolean[] usedRegisters;
    // List of state of the last registers used to pre-load a value
    // that normally is on the stack. 
    private List<Integer> lastRegisters;
    // Number of registers accessible. Only R2 to R(nMax - 1) will be used
    private int nMax;
    private int stackOffset;
    // Number of variables on the virtual stack
    private int nbVarsStack;
    // maximum size of the stack for the TSTO instruction
    private int maxSizeStack;
    private int sizeStack;
    private int classtableSize;

    //public final VariableTable varTable;
    private DecacCompiler compiler;
    private VariableTable namedVars;
    private ClassManager classes;
    private ClassDefinition currentClassDef;

    public void setCurrentClassDef(ClassDefinition def) {
        this.currentClassDef = def;
    }

    public RegisterManager(DecacCompiler compiler, int nb_registres){
        this.compiler = compiler;
        nMax = nb_registres;
        this.classes = null;
        init();
    }

    private void init() {
        this.registers = new int[nMax]; // Initialisé à 0
        this.usedRegisters = new boolean[nMax]; // Initialisé à false
        this.lastRegisters = new LinkedList<Integer>();
        this.nbVarsStack = 0;
        this.maxSizeStack = 0;
        this.sizeStack = 0;
        this.namedVars = null;
        this.stackOffset = 0;
        this.classtableSize = 0;
    }


    /**
     * Prepares a method call, by pushing all the elements required for a method call
     * onto the stack, and putting the first element at the top (since it is the "this")
     * This regman instance should not be used before a BSR, and a SUBSP(nbParams) should
     * be executed before re-using this regman
     */
    public void prepareMethodCall(int nbParams) {
        int nbPrevElems = lastRegisters.size() - nbParams;
        int nbElemsPush = nbParams;
        if (nbPrevElems < 0) {
            nbElemsPush -= nbPrevElems;
            nbPrevElems = 0;
        }
        ListIterator<Integer> regsIterator = lastRegisters.listIterator(nbPrevElems);
        for (int i = 0; i < nbElemsPush; i++) {
            assert(regsIterator.hasNext());
            int regIndex = regsIterator.next();
            regsIterator.remove();
            registers[regIndex] = 0;
            compiler.addInstruction(new PUSH(Register.getR(regIndex)));
            nbVarsStack--;
        }
        assert(!regsIterator.hasNext());
        compiler.addInstruction(new LOAD(new RegisterOffset(-nbParams + 1, Register.SP), Register.R0));
        compiler.addInstruction(new PUSH(Register.R0));
    }

    public void declareMethodVars(ListDeclParam args, ListDeclVar vars,
                                  AbstractIdentifier currentClass) throws DecacFatalError {
        this.currentClassDef = (ClassDefinition) currentClass.getDefinition();
        if (vars == null) {
            throw new DecacFatalError("vars is null");
        }
        LOG.trace("REGMAN declareVars");
        if (namedVars != null) {
            throw new UnsupportedOperationException("Variables already delcared");
        }
        namedVars = new VariableTable(compiler);
        namedVars.addParams(args);
        this.stackOffset = namedVars.init(vars);
        LOG.trace("REGMAN declareVars end");
    }

    public void declareGlobalVars(ListDeclVar vars, int classtableSize) throws DecacFatalError {
        if (vars == null) {
            throw new DecacFatalError("vars is null");
        }
        LOG.trace("REGMAN declareVars");
        if (namedVars != null) {
            throw new UnsupportedOperationException("Variables already delcared");
        }
        namedVars = new VariableTable(compiler);
        this.stackOffset = namedVars.init(vars, classtableSize);
        LOG.trace("REGMAN declareVars end");
        this.classtableSize = classtableSize;
    }

    public int declareClasses(ListDeclClass classDefs, ListDeclImport imports) {
        LOG.trace("REGMAN declareClasses");
        if (classes != null) {
            throw new UnsupportedOperationException("Classes already delcared");
        }
        classes = new ClassManager(compiler, classDefs, imports);
        LOG.trace("REGMAN declareClasses end");
        return classes.getSize();
    }

    public void restoreRegisters() {
        int nbPush = 0;
        for (int i = 0; i < nMax; i++) {
            if (usedRegisters[i]) {
                nbPush++;
                compiler.addFirst(new Line(new PUSH(Register.getR(i))));
                compiler.addInstruction(new POP(Register.getR(i)));
            }
        }
        compiler.addFirst(new Line(new ADDSP(stackOffset)));
        compiler.addFirst(new Line(new BOV(new Label("stack_overflow_error"))));
        compiler.addFirst(new Line(new TSTO(nbPush + maxSizeStack + stackOffset)));
        init(); // reinitalize the inner strucure
    }

    public void endMain() {
        compiler.addFirst(new Line(new ADDSP(stackOffset)));
        compiler.addFirst(new Line("Leave space for local vars"));
        compiler.addFirst(new BOV(new Label("stack_overflow_error")));
        compiler.addFirst(new Line(new TSTO(maxSizeStack + stackOffset)));
        compiler.addFirst(new Line("max size stack: " + maxSizeStack + " offset: " + stackOffset));
        init(); // reinitalize the inner strucure
    }

    public static class RegisterManagerException extends RuntimeException {
    }

    public static class EmptyStackException extends RegisterManagerException {
    }

    public static class AlreadyTakenException extends RegisterManagerException {
    }

    public static class AlreadyHaveException extends RegisterManagerException {
    }

    public static class NoRegisterToUse extends RegisterManagerException {
    }

    public static class InexistingRegister extends RegisterManagerException {
    }

    public int getMaxSizeStack() {
        return maxSizeStack;
    }

    /**
     * calculate the stack's size max
     */
    private void setMaxSizeStack() {
        if (sizeStack > maxSizeStack) {
            maxSizeStack = sizeStack;
        }
    }

    public void addSizeStack(int nb) {
        sizeStack += nb;
        setMaxSizeStack();
    }

    private void decrementSizeStack() {
        sizeStack--;
    }

    public boolean isStackEmpty() {
        return nbVarsStack == 0;
    }

    public int getSizeVirtualStack() {
        return nbVarsStack;
    }

    public void push(GPRegister src) {
        LOG.trace("REGMAN push");
        GPRegister r = take();
        compiler.addInstruction(new LOAD(src, r));
        giveAndPush(r);
        //compiler.addInstruction(new PUSH(src));
        LOG.trace("REGMAN push end");
    }

    public void giveAndPush(GPRegister reg) {
        LOG.trace("REGMAN giveAndPush");
        int i = reg.getNumber();
        usedRegisters[i] = true;
        if (i < 2 || i >= nMax) {
            throw new InexistingRegister();
        }
        if (registers[i] != -1) {
            throw new AlreadyHaveException();
        }
        registers[i] = ++nbVarsStack;
        lastRegisters.add(i);
        LOG.trace("REGMAN giveAndPush end");
    }

    public GPRegister pop() {
        LOG.trace("REGMAN pop");
        if (nbVarsStack < 1) {
            System.err.println("WARNING: stack popped more often than pushed!!");
        }
        if (lastRegisters.size() > 0) {
            int regIndex = lastRegisters.get(lastRegisters.size() - 1);
            if(registers[regIndex] == nbVarsStack) { // Element is in a register
                nbVarsStack -= 1;
                registers[regIndex] = -1;
                lastRegisters.remove(lastRegisters.size() - 1);
                LOG.trace("REGMAN pop end");
                return Register.getR(regIndex);
            }
        }
        // Element is in stack
        GPRegister dst = take();
        nbVarsStack -= 1;
        decrementSizeStack();
        compiler.addInstruction(new POP(dst));
        LOG.trace("REGMAN pop end");
        return dst;
    }

    public void pop(GPRegister dst) {
        LOG.trace("REGMAN pop(dst)");
        int i = dst.getNumber();
        if (i >= 2 && registers[i] != -1) {
            throw new AlreadyHaveException();
        }
        if (nbVarsStack < 1) {
            System.err.println("WARNING: stack popped more often than pushed!!");
        }
        if (lastRegisters.size() > 0) {
            int regIndex = lastRegisters.get(lastRegisters.size() - 1);
            if(registers[regIndex] == nbVarsStack) { // Element is in a register
                nbVarsStack -= 1;
                registers[regIndex] = 0;
                lastRegisters.remove(lastRegisters.size() - 1);
                compiler.addInstruction(new LOAD(Register.getR(regIndex), dst));
                LOG.trace("REGMAN pop(dst) end");
                return;
            }
        }
        // Element is in stack
        nbVarsStack -= 1;
        decrementSizeStack();
        compiler.addInstruction(new POP(dst));
        LOG.trace("REGMAN pop(dst) end");
    }

    private void take(GPRegister register) {
        LOG.trace("REGMAN take(reg)");
        usedRegisters[register.getNumber()] = true;
        if (register.getNumber() < 0 || register.getNumber() >= nMax) {
            throw new InexistingRegister();
        }
        if (registers[register.getNumber()] == -1) {
            throw new AlreadyTakenException();
        }
        if (registers[register.getNumber()] != 0) { // Register is used
            registers[register.getNumber()] = -1;
            int saveIndex = lastRegisters.indexOf(register.getNumber());
            assert(saveIndex != -1);
            lastRegisters.remove(saveIndex);
            addSizeStack(1);
            compiler.addInstruction(new PUSH(register));
        } else { // Register is unused
            registers[register.getNumber()] = -1;
        }
        LOG.trace("REGMAN take(reg) end");
    }

    public GPRegister take() {
        LOG.trace("REGMAN take");
        int min = -1;
        int min_index = -1;
        for (int i = 2; i < nMax; i++) {
            if(registers[i] == 0) {
                min_index = i;
                break;
            }
            if (registers[i] == -1) {
                continue;
            }
            if (min == -1 || min > registers[i]) {
                min = registers[i];
                min_index = i;
            }
        }
        if (min_index == -1) {
            throw new NoRegisterToUse();
        }
        GPRegister r = Register.getR(min_index);
        take(r);
        LOG.trace("REGMAN take end");
        return r;
    }

    public void give(GPRegister register) {
        LOG.trace("REGMAN give");
        if (register.getNumber() < 2 || register.getNumber() >= nMax) {
            throw new InexistingRegister();
        }
        if (registers[register.getNumber()] != -1) {
            throw new AlreadyHaveException();
        }
        registers[register.getNumber()] = 0;
        LOG.trace("REGMAN give end");
    }

    public boolean isTaken(GPRegister register) {
        LOG.trace("REGMAN isTaken");
        if (register.getNumber() < 2 || register.getNumber() >= nMax) {
            throw new InexistingRegister();
        }
        LOG.trace("REGMAN isTaken end");
        return registers[register.getNumber()] == -1;
    }

    public boolean isUsed(GPRegister register) {
        LOG.trace("REGMAN isUsed");
        if (register.getNumber() < 2 || register.getNumber() >= nMax) {
            throw new InexistingRegister();
        }
        LOG.trace("REGMAN isUsed end");
        return registers[register.getNumber()] != 0;
    }

    public void load(Symbol s, GPRegister dst) {
        LOG.trace("REGMAN load");
        if (namedVars == null || !namedVars.load(s, dst)) {
            assert(currentClassDef != null);
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), dst));
            try {
                getField(dst, s, currentClassDef, dst);
            } catch (DecacFatalError e) {
                throw new RuntimeException("Error while loading on implicit this", e);
            }
        }
        LOG.trace("REGMAN load end");
    }
    
    public GPRegister load(Symbol s) {
        LOG.trace("REGMAN load");
        GPRegister dst = this.take();
        load(s, dst);
        LOG.trace("REGMAN load end");
        return dst;
    }

    public void store(Symbol s, GPRegister register) {
        LOG.trace("REGMAN store");
        if (namedVars == null || !namedVars.store(s, register)) {
            assert(currentClassDef != null);
            GPRegister addr = take();
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), addr));
            try {
                setField(addr, s, currentClassDef, register);
            } catch (DecacFatalError e) {
                throw new RuntimeException("Error while storing on implicit this", e);
            }
            give(addr);
        }
        LOG.trace("REGMAN store end");
    }

    public void giveAndStore(Symbol s, GPRegister register) {
        LOG.trace("REGMAN giveAndStore");
        store(s, register);
        give(register);
        LOG.trace("REGMAN giveAndStore end");
    }

    public void getField(GPRegister addr, Symbol fieldName, Definition objDef,
                         GPRegister dst) throws DecacFatalError {
        classes.getField(addr, fieldName, objDef, dst);
    }

    public GPRegister getField(GPRegister addr, Symbol fieldName, Definition objDef)
            throws DecacFatalError {
        GPRegister dst = take();
        classes.getField(addr, fieldName, objDef, dst);
        return dst;
    }

    public void setField(GPRegister addr, Symbol fieldName, Definition objDef,
                         GPRegister src) throws DecacFatalError {
        classes.setField(addr, fieldName, objDef, src);
    }

    public void getMethod(GPRegister addr, Symbol methName, Definition objDef,
                         GPRegister dst) throws DecacFatalError {
        classes.getMethod(addr, methName, objDef, dst);
    }

    public GPRegister getMethod(GPRegister addr, Symbol methName, Definition objDef)
            throws DecacFatalError {
        GPRegister dst = take();
        classes.getMethod(addr, methName, objDef, dst);
        return dst;
    }
}
