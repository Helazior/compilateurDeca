package fr.ensimag.deca.codegen;

import java.util.Map;
import java.util.HashMap;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class ClassManager {
    private DecacCompiler compiler;
    private int tablesSize;
    public int getSize() {
        return tablesSize;
    }
    public ClassManager(DecacCompiler compiler, ListDeclClass classes) {
        this.compiler = compiler;
        tablesSize = 0;
        EnvironmentType types = compiler.getTypeEnv();
        IMAProgram classTableInitsFns = new IMAProgram();
        IMAProgram classTableInitsMain = new IMAProgram();

        classTableInitsFns.addComment("---------------------------------------------------");
        classTableInitsFns.addComment("    Construction tables des methodes");
        classTableInitsFns.addComment("---------------------------------------------------");
        classTableInitsFns.addComment("---------------------Object------------------------");
        classTableInitsFns.addLabel(new Label("classTableInit.Object"));
        classTableInitsFns.addInstruction(new LOAD(new NullOperand(), Register.R1));
        classTableInitsFns.addInstruction(new STORE(
            Register.R1,
            new RegisterOffset(1, Register.SP)));

        classTableInitsFns.addInstruction(new LOAD(
            new LabelOperand(new Label("methodBody.Object.equals")),
            Register.R1));
        classTableInitsFns.addInstruction(new STORE(
            Register.R1, new RegisterOffset(2, Register.SP)));

        // Compute table size and build function
        tablesSize = 3;
        for (AbstractDeclClass classDef : classes.getList()) {
            int classTableSize = classDef.codeGenClassTableFn(compiler, classTableInitsFns, tablesSize);
            tablesSize += classTableSize;
        }
        // Call functions and build super reference
        for (AbstractDeclClass classDef : classes.getList()) {
            classDef.codeGenClassTableMain(compiler, classTableInitsMain);
        }
        Label end = new Label("classTableInit_end");
        classTableInitsMain.addInstruction(new BRA(end));
        classTableInitsMain.append(classTableInitsFns);
        classTableInitsMain.addLabel(end);
        classTableInitsMain.addInstruction(new ADDSP(tablesSize));
        classTableInitsMain.addFirst(new Line(new TSTO(tablesSize)));
        compiler.concatenateEndProgram(classTableInitsMain);
    }

    /** Loads the field's content into a register */
    public void getField(GPRegister reg, Symbol fieldName, Definition objDef,
                         GPRegister dst, Location l) throws DecacFatalError {
        if (!objDef.isClass()) {
            throw new DecacFatalError(objDef.getType().getName() + " should be a class");
        }
        ClassDefinition typeDef = (ClassDefinition) objDef;
        ExpDefinition exp = typeDef.getMembers().get(fieldName);
        int index = ((FieldDefinition) exp).getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(index, reg), dst));
    }

    public void setField(GPRegister addr, Symbol fieldName, Definition objDef,
                         GPRegister src, Location l) throws DecacFatalError {
        if (!objDef.isClass()) {
            throw new DecacFatalError(objDef.getType().getName() + " should be a class");
        }
        ClassDefinition typeDef = (ClassDefinition) objDef;
        ExpDefinition exp = typeDef.getMembers().get(fieldName);
        int index = ((FieldDefinition) exp).getIndex();
        compiler.addInstruction(new STORE(src, new RegisterOffset(index, addr)));
    }

    /** Loads the method's address into a register */
    public void getMethod(GPRegister reg, Symbol methName, Definition objDef,
                         GPRegister dst, Location l) throws DecacFatalError {
        if (!objDef.isClass()) {
            throw new DecacFatalError(objDef.getType().getName() + " should be a class");
        }
        ClassDefinition typeDef = (ClassDefinition) objDef;
        ExpDefinition exp = typeDef.getMembers().get(methName);
        int index = ((MethodDefinition) exp).getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(index, reg), dst));
    }
}
