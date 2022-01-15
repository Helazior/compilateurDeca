package fr.ensimag.deca.codegen;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import fr.ensimag.deca.codegen.VariableTable;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

public class RegisterManager {
    private static final Logger LOG = Logger.getLogger(RegisterManager.class);
    // List of state of the registers. 
    // -1: externally used.
    //  0: Not used.
    // > 0: used for the stacked variable of this index
    private final int[] registers;
    // List of state of the last registers used to pre-load a value
    // that normally is on the stack. 
    private final List<Integer> lastRegisters;
    // Number of registers accessible. Only R2 to R(nMax - 1) will be used
    private final int nMax;
    // Number of variables on stack
    private int nbVarsStack = 0;

    //public final VariableTable varTable;
    private DecacCompiler compiler;
    private VariableTable namedVars;

    public RegisterManager(DecacCompiler compiler, int nb_registres){
        this.compiler = compiler;
        //varTable = new VariableTable(compiler, vars);
        nMax = nb_registres;
        this.registers = new int[nb_registres]; // Initialisé à 0
        this.lastRegisters = new ArrayList<Integer>(); // Initialisé à 0
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

    public boolean isStackEmpty() {
        return nbVarsStack == 0;
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
        int regIndex = lastRegisters.get(lastRegisters.size() - 1);
        if(registers[regIndex] == nbVarsStack) { // Element is in a register
            nbVarsStack -= 1;
            registers[regIndex] = -1;
            lastRegisters.remove(lastRegisters.size() - 1);
            LOG.trace("REGMAN pop end");
            return Register.getR(regIndex);
        }
        // Element is in stack
        GPRegister dst = take();
        nbVarsStack -= 1;
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
        compiler.addInstruction(new POP(dst));
        LOG.trace("REGMAN pop(dst) end");
    }

    public void take(GPRegister register) {
        LOG.trace("REGMAN take(reg)");
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

    public void declareVars(ListDeclVar vars) {
        LOG.trace("REGMAN declareVars");
        if (namedVars != null) {
            throw new UnsupportedOperationException("Not able to declare new variables");
        }
        namedVars = new VariableTable(compiler);
        namedVars.init(vars);
        LOG.trace("REGMAN declareVars end");
    }

    public void load(Symbol s, GPRegister dst) {
        LOG.trace("REGMAN load");
        assert(namedVars != null);
        namedVars.load(s, dst);
        LOG.trace("REGMAN load end");
    }
    
    public GPRegister load(Symbol s) {
        LOG.trace("REGMAN load");
        assert(namedVars != null);
        GPRegister dst = this.take();
        load(s, dst);
        LOG.trace("REGMAN load end");
        return dst;
    }

    public void store(Symbol s, GPRegister register) {
        LOG.trace("REGMAN store");
        assert(namedVars != null);
        namedVars.store(s, register);
        LOG.trace("REGMAN store end");
    }

    public void giveAndStore(Symbol s, GPRegister register) {
        LOG.trace("REGMAN giveAndStore");
        assert(namedVars != null);
        store(s, register);
        give(register);
        LOG.trace("REGMAN giveAndStore end");
    }
}
