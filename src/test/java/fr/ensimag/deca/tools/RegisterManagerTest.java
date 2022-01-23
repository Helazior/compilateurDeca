package fr.ensimag.deca.tools;

import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.*;
import org.junit.jupiter.api.Test;
import fr.ensimag.ima.pseudocode.*;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterManagerTest {
    public static void main(String[] args) {
        RegisterManagerTest test = new RegisterManagerTest();
        test.testPushPop();
        //test.testOwnership();
    }

    private void assertProgram(String out, String[] req) {
        String[] res = out.strip().split("\n");
        int i;
        for (i = 0; i < res.length; i++) {
            assertTrue(i < req.length);
            assertEquals(res[i].strip(), req[i].strip());
        }
        assertEquals(i, req.length);
    }

    @Test
    public void testPushPop() {
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        RegisterManager rm = new RegisterManager(compiler, 6);
        rm.push(Register.R1);
        rm.pop(Register.R0);
        String programm = compiler.displayIMAProgram();
        String[] req = {
            "LOAD R1, R2",
            "LOAD R2, R0"
        };
        assertProgram(programm, req);
    }

    //@Test
    public void testOwnership() {
        /*DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        RegisterManager rm = new RegisterManager(compiler, 6);
        for (int i = 2; i < 6; i++) {
            assertFalse(rm.isTaken(Register.getR(i)));
        }
        rm.take(Register.R3);
        rm.take(Register.R5);
        assertFalse(rm.isTaken(Register.R2));
        assertTrue(rm.isTaken(Register.R3));
        assertFalse(rm.isTaken(Register.R4));
        assertTrue(rm.isTaken(Register.R5));

        boolean success = false;
        try {
            rm.isTaken(Register.R6);
        } catch(RegisterManager.InexistingRegister e) {
            success = true;
        }
        assertTrue(success);
        success = false;
        try {
            rm.take(Register.R3);
        } catch(RegisterManager.AlreadyTakenException e) {
            success = true;
        }
        assertTrue(success);
        rm.take(Register.R2);
        rm.give(Register.R3);
        assertFalse(rm.isTaken(Register.R3));
        success = false;
        try {
            rm.give(Register.R3);
        } catch(RegisterManager.AlreadyHaveException e) {
            success = true;
        }
        assertTrue(success);*/
    }
}