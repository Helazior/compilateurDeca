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
    public ClassManager(DecacCompiler compiler, ListDeclClass classes, ListDeclImport imports) {
        this.compiler = compiler;
        tablesSize = 0;
        EnvironmentType types = compiler.getTypeEnv();
        IMAProgram classTableInitsFns = new IMAProgram();
        IMAProgram classTableInitsMain = new IMAProgram();


        // Compute table size and build function
        classTableInitsFns.addComment("---------------------------------------------------");
        classTableInitsFns.addComment("    Construction tables des methodes");
        classTableInitsFns.addComment("---------------------------------------------------");
        classTableInitsFns.addComment("-------------------- Object -----------------------");
        classTableInitsFns.addLabel(new Label("classTableInit.Object"));

        classTableInitsFns.addInstruction(new LOAD(
                new LabelOperand(new Label("methodBody.Object.equals")),
                Register.R0));
        classTableInitsFns.addInstruction(new STORE(
                Register.R0, new RegisterOffset(1, Register.R1)));
        classTableInitsFns.addInstruction(new RTS());
        tablesSize = genTableFunctions(classTableInitsFns, classes, imports, 3);

        // Call functions and build super reference
        classTableInitsMain.addInstruction(new BSR(new Label("classTableInit.Object")));
        classTableInitsMain.addInstruction(new LOAD(new NullOperand(), Register.R1));
        classTableInitsMain.addInstruction(new STORE(Register.R1, new RegisterOffset(1, Register.GB)));
        classTableInitsMain.addFirst(new LEA(new RegisterOffset(1, Register.GB), Register.R1));
        classTableInitsMain.addFirst(new Line(new ADDSP(3)));
        
        genTableMain(classTableInitsMain, classes, imports);

        Label end = new Label("classTableInit_end");
        classTableInitsMain.addInstruction(new BRA(end));
        classTableInitsMain.append(classTableInitsFns);
        classTableInitsFns.addComment("-------------------- Fin tables -----------------------");
        classTableInitsMain.addLabel(end);
        classTableInitsMain.addFirst(new Line(new BOV(new Label("stack_overflow_error"))));
        classTableInitsMain.addFirst(new Line(new TSTO(tablesSize)));
        compiler.concatenateEndProgram(classTableInitsMain);
    }

    private int genTableFunctions(IMAProgram program, ListDeclClass classes, ListDeclImport imports, int tablesSize) {
        program.addComment("------------------- Imports Fn ----------------------");
        if (imports != null) {
            for (AbstractDeclImport imp : imports.getList()) {
                AbstractProgram prog = imp.getProgram();
                tablesSize = genTableFunctions(program, prog.getClasses(), prog.getImports(), tablesSize);
            }
        }
        program.addComment("----------------- End Imports Fn --------------------");
        for (AbstractDeclClass classDef : classes.getList()) {
            int classTableSize = classDef.codeGenClassTableFn(compiler, program, tablesSize);
            tablesSize += classTableSize;
        }
        return tablesSize;
    }

    private void genTableMain(IMAProgram program, ListDeclClass classes, ListDeclImport imports) {
        program.addComment("------------------ Imports Main ---------------------");
        if (imports != null) {
            for (AbstractDeclImport imp : imports.getList()) {
                AbstractProgram prog = imp.getProgram();
                genTableMain(program, prog.getClasses(), prog.getImports());
            }
        }
        program.addComment("---------------- End Imports Main -------------------");
        for (AbstractDeclClass classDef : classes.getList()) {
            classDef.codeGenClassTableMain(compiler, program);
        }
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
        compiler.addInstruction(new LOAD(new RegisterOffset(index + 1, reg), dst));
    }

    public void setField(GPRegister addr, Symbol fieldName, Definition objDef,
                         GPRegister src, Location l) throws DecacFatalError {
        if (!objDef.isClass()) {
            throw new DecacFatalError(objDef.getType().getName() + " should be a class");
        }
        ClassDefinition typeDef = (ClassDefinition) objDef;
        ExpDefinition exp = typeDef.getMembers().get(fieldName);
        int index = ((FieldDefinition) exp).getIndex();
        compiler.addInstruction(new STORE(src, new RegisterOffset(index + 1, addr)));
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
