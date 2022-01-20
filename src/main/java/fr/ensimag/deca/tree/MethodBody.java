package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;
import java.util.ArrayList;

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
    protected void codeGenMethod(DecacCompiler compiler, ListDeclParam args) {
        // TODO : arguments = registres ?
        // déclare les variables locales et les arguments de la méthode
        compiler.getRegMan().declareMethodVars(args, declVariables);

        compiler.addComment("Beginning of method instructions:");
        insts.codeGenListInst(compiler);
    }

    /**
    * Contains no real information => nothing to check.
    */
    @Override
    protected void checkLocation() {
        throw new UnsupportedOperationException("not yet implemented");    }

    @Override
    public void decompile(IndentPrintStream s) {

        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
