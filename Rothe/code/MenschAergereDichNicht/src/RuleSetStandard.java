/**
 * Standard rule set implementation.
 * Defines rules for a typical "Mensch ärgere dich nicht" game.
 */
public class RuleSetStandard implements I_RuleSet {
    private static final int NUM_FIELDS_PER_PLAYER = 5;
    private final int playerCount;

    public RuleSetStandard(int playerCount) {
        if (playerCount <= 0) throw new IllegalArgumentException("playerCount must be positive");
        this.playerCount = playerCount;
    }

    @Override
    public boolean checkRoll(GameManager gameManager) {
        // In the standard rules, a player can always roll
        // (in more complex variants, this might check for trapped figures, etc.)
        return true;
    }

    @Override
    public boolean checkWin(GameManager gameManager) {
        // A player wins when all their figures reach the goal
        // (Goal detection would require figure position tracking or a goal field type)
        // For now, we return false as a placeholder
        return false;
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS_PER_PLAYER * playerCount;
    }
}
