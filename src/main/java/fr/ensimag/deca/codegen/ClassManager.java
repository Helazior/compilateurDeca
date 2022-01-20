package fr.ensimag.deca.codegen;

import java.util.Map;
import java.util.HashMap;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class ClassManager {
    private DecacCompiler compiler;
    public ClassManager(DecacCompiler compiler, ListDeclClass classes) {
        this.compiler = compiler;
        EnvironmentType types = compiler.getTypeEnv();
        for (AbstractDeclClass classDef : classes.getList()) {
            // TODO
        }
    }

    /** Loads the field's content into a register */
    public void getField(GPRegister reg, Symbol fieldName, Type objType,
                         GPRegister dst, Location l) throws ContextualError {
        ClassDefinition typeDef = objType.asClassType(
            "Error: " + objType.getName() + " is not a class type.", l).getDefinition();
        ExpDefinition exp = ((ClassDefinition) typeDef).getMembers().get(fieldName);
        int index = exp.asFieldDefinition(
            "Identifier " + fieldName + " in class " + objType + " is not a field !",
            l).getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(index, reg), dst));
    }

    public void setField(GPRegister addr, Symbol fieldName, Type objType,
                         GPRegister src, Location l) throws ContextualError {
        ClassDefinition typeDef = objType.asClassType(
            "Error: " + objType.getName() + " is not a class type.", l).getDefinition();
        ExpDefinition exp = ((ClassDefinition) typeDef).getMembers().get(fieldName);
        int index = exp.asFieldDefinition(
            "Identifier " + fieldName + " in class " + objType + " is not a field !",
            l).getIndex();
        compiler.addInstruction(new STORE(src, new RegisterOffset(index, addr)));
    }

    /** Loads the method's address into a register */
    public void getMethod(GPRegister reg, Symbol methName, Type objType,
                         GPRegister dst, Location l) throws ContextualError {
        ClassDefinition typeDef = objType.asClassType(
            "Error: " + objType.getName() + " is not a class type.", l).getDefinition();
        ExpDefinition exp = ((ClassDefinition) typeDef).getMembers().get(methName);
        int index = exp.asMethodDefinition(
            "Identifier " + methName + " in class " + objType + " is not a field !",
            l).getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(index, reg), dst));
    }
}
