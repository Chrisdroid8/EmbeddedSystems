import java.util.List;

/**
 * Interface for rule sets that define game mechanics.
 * Implementers define when a player may roll, when they win, and board size.
 */
public interface I_RuleSet {
    /**
     * Check if the active player is allowed to roll the die.
     * 
     * @param player the active player
     * @return {@code true} if the player may roll, {@code false} otherwise
     */
    boolean checkRoll(Player player);

    /**
     * Check which figures the active player can move based on the roll value.
     * 
     * @param player the active player
     * @param rollValue the value rolled by the player
     * @return a list of {@link GameFigure} that can be moved, or an empty list if no move is possible
     */
    List<GameFigure> checkMove(Player player, int rollValue);

    /**
     * Check if the active player has won the game.
     * 
     * @param player the active player
     * @return {@code true} if the player has won, {@code false} otherwise
     */
    boolean checkWin(Player player);

    /**
     * Get the total number of fields on the board.
     * 
     * @return the number of board fields
     */
    int getNumFields();


    /**
     * Resets the Last action per Turn
     * 
     */
    void resetLastAction();
}
