package fr.ensimag.deca.codegen;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractDeclVar;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;

public class VariableTable {
    private Map<Symbol, Integer> table = new HashMap<>();
    private int nextKey = 1;
    private DecacCompiler compiler;
    public VariableTable(DecacCompiler compiler, ListDeclVar vars) {
        this.compiler = compiler;
        List<AbstractDeclVar> varsList = vars.getList();
        compiler.addInstruction(new ADDSP(varsList.size()));
        for (int i = 0; i < varsList.size(); i++) {
            AbstractDeclVar variable = varsList.get(i);
            Symbol varname = variable.codeGenDecl(compiler, nextKey + i).getName();
            table.put(varname, nextKey + i);
        }
        nextKey += varsList.size();
    }
    public void load(Symbol s, GPRegister dst) {
        int i = get(s);
        compiler.addInstruction(new LOAD(new RegisterOffset(i, Register.LB), dst));
    }
    public void store(Symbol s, GPRegister register) {
        int i = get(s);
        compiler.addInstruction(new STORE(register, new RegisterOffset(i, Register.LB)));
    }
    public int get(Symbol s) {
        return table.get(s);
    }
}
