package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;

/**
 * Exception raised when a contextual type error is found.
 *
 * @author gl60
 * @date 01/01/2022
 */
public class ContextualPrintTypeError extends ContextualError {

    public ContextualPrintTypeError(Type wrongType, Location location) {
        super("Can't print a value of type " + wrongType.getName().getName()
              + ". Only integer, floats and strings are allowed.", location);
    }
}
