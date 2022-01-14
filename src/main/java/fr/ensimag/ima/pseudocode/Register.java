package fr.ensimag.ima.pseudocode;

/**
 * Register operand (including special registers like SP).
 * 
 * @author Ensimag
 * @date 01/01/2022
 */
public class Register extends DVal {
    private String name;
    protected Register(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Global Base register
     */
    public static final Register GB = new Register("GB");
    /**
     * Local Base register
     */
    public static final Register LB = new Register("LB");
    /**
     * Stack Pointer
     */
    public static final Register SP = new Register("SP");
    /**
     * General Purpose Registers. Array is private because Java arrays cannot be
     * made immutable, use getR(i) to access it.
     */
    private static final GPRegister[] R = initRegisters();
    /**
     * General Purpose Registers
     */
    public static GPRegister getR(int i) {
        return R[i];
    }
    /**
     * Convenience shortcut for R[0]
     */
    public static final GPRegister R0 = R[0];
    /**
     * Convenience shortcut for R[1]
     */
    public static final GPRegister R1  = R[1];
    public static final GPRegister R2  = R[2];
    public static final GPRegister R3  = R[3];
    public static final GPRegister R4  = R[4];
    public static final GPRegister R5  = R[5];
    public static final GPRegister R6  = R[6];
    public static final GPRegister R7  = R[7];
    public static final GPRegister R8  = R[8];
    public static final GPRegister R9  = R[9];
    public static final GPRegister R10 = R[10];
    public static final GPRegister R11 = R[11];
    public static final GPRegister R12 = R[12];
    public static final GPRegister R13 = R[13];
    public static final GPRegister R14 = R[14];
    public static final GPRegister R15 = R[15];
    static private GPRegister[] initRegisters() {
        GPRegister [] res = new GPRegister[16];
        for (int i = 0; i <= 15; i++) {
            res[i] = new GPRegister("R" + i, i);
        }
        return res;
    }
}
