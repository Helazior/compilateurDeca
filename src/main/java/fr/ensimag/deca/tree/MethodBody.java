package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;

public class MethodBody extends AbstractMethodBody{

    private ListDeclVar declVariables;
    private ListInst insts;

    public MethodBody(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
        }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp methodEnv,
            AbstractIdentifier currentClass, Type returnType) throws ContextualError {
        ClassDefinition classDef = (ClassDefinition)compiler.getType(currentClass.getName());

        declVariables.verifyListDeclVariable(compiler, methodEnv, classDef);
        insts.verifyListInst(compiler, methodEnv, classDef, returnType);
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
    * Contains no real information => nothing to check.
    */
    @Override
    protected void checkLocation() {
        throw new UnsupportedOperationException("not yet implemented");    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("not yet implemented");    }
}
