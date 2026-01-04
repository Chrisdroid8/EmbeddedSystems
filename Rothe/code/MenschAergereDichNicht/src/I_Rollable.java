/**
 * Interface for rollable objects (e.g., dice).
 * Implementations must provide a way to roll and return a result.
 */
public interface I_Rollable {
    /**
     * Roll this object and return the result.
     *
     * @return integer result of the roll
     */
    int roll();
}
