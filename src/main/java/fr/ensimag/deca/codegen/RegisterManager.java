package fr.ensimag.deca.codegen;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import fr.ensimag.deca.codegen.VariableTable;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.*;

public class RegisterManager {
    // List of state of the registers. 
    // -1: externally used.
    //  0: Not used.
    // > 0: used for the stacked variable of this index
    private final int[] registers;
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

    public void push(GPRegister src) {
        nbVarsStack += 1;
        compiler.addInstruction(new PUSH(src));
    }

    public void pop(GPRegister dst) {
        if (nbVarsStack < 1) {
            throw new EmptyStackException();
        }
        nbVarsStack -= 1;
        compiler.addInstruction(new POP(dst));
    }

    public GPRegister pop() {
        if (nbVarsStack < 1) {
            throw new EmptyStackException();
        }
        GPRegister dst = takeUnused();
        nbVarsStack -= 1;
        compiler.addInstruction(new POP(dst));
        return dst;
    }

    public void giveAndPush(GPRegister reg) {
        push(reg);
        give(reg);
    }

    public void take(GPRegister register) {
        if (register.getNumber() < 0 || register.getNumber() >= nMax) {
            throw new InexistingRegister();
        }
        if (registers[register.getNumber()] == -1) {
            throw new AlreadyTakenException();
        }
        if (registers[register.getNumber()] != 0) {
            push(register);
        }
        registers[register.getNumber()] = -1;
    }

    public GPRegister take() {
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
        return r;
    }

    public void give(GPRegister register) {
        if (register.getNumber() < 0 || register.getNumber() >= nMax) {
            throw new InexistingRegister();
        }
        if (registers[register.getNumber()] != -1) {
            throw new AlreadyHaveException();
        }
        registers[register.getNumber()] = 0;
    }

    public boolean isTaken(GPRegister register) {
        if (register.getNumber() < 0 || register.getNumber() >= nMax) {
            throw new InexistingRegister();
        }
        return registers[register.getNumber()] == -1;
    }

    public boolean isUsed(GPRegister register) {
        if (register.getNumber() < 0 || register.getNumber() >= nMax) {
            throw new InexistingRegister();
        }
        return registers[register.getNumber()] != 0;
    }

    public void declareVars(ListDeclVar vars) {
        if (namedVars != null) {
            throw new UnsupportedOperationException("Not able to declare new variables");
        }
        namedVars = new VariableTable(compiler, vars);
    }

    public void load(Symbol s, GPRegister dst) {
        assert(namedVars != null);
        namedVars.load(Symbol s, GPRegister dst);
    }
    
    public GPRegister load(Symbol s) {
        assert(namedVars != null);
        GPRegister dst = this.takeUnused();
        load(s, dst);
        return dst;
    }

    public void store(Symbol s, GPRegister register) {
        assert(namedVars != null);
        namedVars.store(s, register);
    }

    public void giveAndStore(Symbol s, GPRegister register) {
        assert(namedVars != null);
        store(s, register);
        give(register);
    }
}
