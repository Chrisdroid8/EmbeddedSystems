/**
 * Interface for rule sets that define game mechanics.
 * Implementers define when a player may roll, when they win, and board size.
 */
public interface I_RuleSet {
    /**
     * Check if the active player is allowed to roll the die.
     * 
     * @param gameManager the game manager managing the game state
     * @return {@code true} if the player may roll, {@code false} otherwise
     */
    boolean checkRoll(GameManager gameManager);

    /**
     * Check if the active player has won the game.
     * 
     * @param gameManager the game manager managing the game state
     * @return {@code true} if the player has won, {@code false} otherwise
     */
    boolean checkWin(GameManager gameManager);

    /**
     * Get the total number of fields on the board.
     * 
     * @return the number of board fields
     */
    int getNumFields();
}
