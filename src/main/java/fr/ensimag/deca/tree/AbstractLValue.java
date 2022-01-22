package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl60
 * @date 01/01/2022
 */
public abstract class AbstractLValue extends AbstractExpr {
    protected abstract void codeGenStoreLValue(DecacCompiler compiler) throws ContextualError;
//     codeGenGetLValue(compiler);
//    id rien à faire;
//            selection exec membre de gauche;
}

