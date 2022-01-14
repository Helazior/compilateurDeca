package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl60
 * @date 01/01/2022
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        // TODO: à tester
        RegisterManager regMan = compiler.getRegMan();

        compiler.addInstruction(new FLOAT(Register.R0, Register.R1));
        regMan.push(Register.R1);
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
