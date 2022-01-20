package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import org.apache.log4j.Logger;

/**
 *
 * @author gl60
 * @date 14/01/2022
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    //TODO
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField declField : getList()) {
            declField.decompile(s);
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListFieldVisibility(DecacCompiler compiler,
            AbstractIdentifier superClass, AbstractIdentifier currentClass)
            throws ContextualError {

        for (AbstractDeclField declField : getList()) {
            declField.verifyFieldVisibility(compiler, superClass, currentClass);

            ClassDefinition currClassDef = (ClassDefinition)compiler.getType(currentClass.getName());
            currClassDef.incNumberOfFields();
        }
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListFieldType(DecacCompiler compiler, AbstractIdentifier currentClass)
            throws ContextualError {

        for (AbstractDeclField declField : getList()) {
            declField.verifyFieldType(compiler, currentClass);
        }
    }

}

