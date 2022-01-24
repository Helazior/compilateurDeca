package fr.ensimag.deca.codegen;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractDeclParam;
import fr.ensimag.deca.tree.AbstractDeclVar;
import fr.ensimag.deca.tree.ListDeclParam;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;

public class VariableTable {
    private Map<Symbol, Integer> table = new HashMap<>();
    private int nextKey = 1;
    private DecacCompiler compiler;
    public VariableTable(DecacCompiler compiler) {
        this.compiler = compiler;
    }
    public int init(ListDeclVar vars, int classtableSize) throws DecacFatalError {
        nextKey = classtableSize;
        List<AbstractDeclVar> varsList = vars.getList();
        for (int i = 0; i < varsList.size(); i++) {
            AbstractDeclVar variable = varsList.get(i);
            Symbol varname = variable.getName().getName();
            table.put(varname, nextKey + i);
        }
        nextKey += varsList.size();
        // add the number of variables in the TSTO stack's max size
        compiler.getRegMan().addSizeStack(varsList.size());
        initalizeVars(vars, classtableSize);
        return varsList.size();
    }
    private void initalizeVars(ListDeclVar vars, int classtableSize) throws DecacFatalError {
        int localNextKey = classtableSize;
        List<AbstractDeclVar> varsList = vars.getList();
        for (int i = 0; i < varsList.size(); i++) {
            AbstractDeclVar variable = varsList.get(i);
            variable.codeGenDecl(compiler, localNextKey + i);
        }
    }
    public void addParams(ListDeclParam params) throws DecacFatalError {
        List<AbstractDeclParam> paramsList = params.getList();
        for (int i = 0; i < paramsList.size(); i++) {
            AbstractDeclParam param = paramsList.get(paramsList.size() - i - 1);
            table.put(param.getName(), -3 - i);
        }
    }
    public int init(ListDeclVar vars) throws DecacFatalError {
        return init(vars, 1);
    }
    public boolean load(Symbol s, GPRegister dst) {
        Integer i = get(s);
        if (i == null) {
            return false;
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(i, Register.LB), dst));
        return true;
    }
    public boolean store(Symbol s, GPRegister register) {
        Integer i = get(s);
        if (i == null) {
            return false;
        }
        compiler.addInstruction(new STORE(register, new RegisterOffset(i, Register.LB)));
        return true;
    }
    public Integer get(Symbol s) {
        return table.get(s);
    }
}
