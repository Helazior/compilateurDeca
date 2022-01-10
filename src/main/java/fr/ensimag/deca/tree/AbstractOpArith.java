package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

import static fr.ensimag.ima.pseudocode.Register.R1;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public void codeGenOp(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler) {
        //super.codeGenExpr(compiler);
        AbstractExpr left = getLeftOperand();
        left.codeGenExpr(compiler);
        AbstractExpr right = getRightOperand();
        right.codeGenExpr(compiler);
        compiler.addComment("codeGenExpr AbstractOpArith -> Début parcours");

        compiler.addInstruction(new POP(Register.R0));
        compiler.addInstruction(new POP(Register.R1));
        compiler.addComment("codeGenExpr AbstractOpArith -> Fin parcours");
        codeGenOp(compiler);
        compiler.addInstruction(new PUSH(Register.R1));
    }

    protected void codeGenPrint(DecacCompiler compiler) {
        // TODO : C'est ça ou pas ? Ça a l'air de marcher mais pas sûr à 100%
        codeGenExpr(compiler);
        compiler.addInstruction(new POP(R1));
        // si type = int
        Type type = getType();
        if (type.isInt()) {
            compiler.addInstruction(new WINT());
        } else if(type.isFloat()) {
            compiler.addInstruction(new WFLOAT());
        }
        //throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
