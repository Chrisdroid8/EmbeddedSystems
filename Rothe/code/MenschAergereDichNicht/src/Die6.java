/**
 * A standard 6-sided die.
 * Rolls return an integer in the range [1, 6].
 */
public class Die6 implements I_Rollable {
    private final java.util.Random random;

    /**
     * Create a new 6-sided die.
     */
    public Die6() {
        this.random = new java.util.Random();
    }

    /**
     * Roll this die and return a value between 1 and 6 (inclusive).
     *
     * @return integer in range [1, 6]
     */
    @Override
    public int roll() {
        return this.random.nextInt(6) + 1;
    }
}
