import java.util.List;
import java.util.Map;
import java.util.HashMap;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractDeclVar;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;

public class VariableTable {
    private HashMap<Symbol, Integer> table = new HashMap<>();
    private int nextKey = 1;
    public VariableTable(ListDeclVar vars) {
        List<AbstractDeclVar> varsList = vars.getList();
        for (int i = 0; i < varsList.size(); i++) {
            AbstractDeclVar variable = varsList.get(i);
            Symbol varname = variable.codeGenDecl(new ImmediateInteger(nextKey + i)).getName();
            table.put(varname, nextKey + i);
        }
        nextKey += varsList.size();
    }
    public void load(Symbol s, GPRegister dst) {
        int i = get(s);
        new LOAD(new RegisterOffset(i, Register.LB), dst);
    }
    public void store(Symbol s, GPRegister register) {
        int i = get(s);
        new STORE(register, new RegisterOffset(i, Register.LB));
    }
    public int get(Symbol s) {
        return table.get(s);
    }
}
