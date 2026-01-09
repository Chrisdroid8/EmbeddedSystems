/**
 * Interface for game visualization.
 * Implementers can display the game state in various ways (console, GUI, etc.).
 */
public interface I_Visual {
    /**
     * Display the current state of the game board and all figures.
     * 
     * @param fields the array of all game fields
     * @param players the array of all players in the game
     */
    void displayGameState(Field[] fields, Player[] players);

    /**
     * Display a message indicating whose turn it is.
     * 
     * @param player the current player
     */
    void displayCurrentPlayer(Player player);

    /**
     * Display the result of a die roll.
     * 
     * @param player the player who rolled
     * @param rollValue the value rolled
     */
    void displayRoll(Player player, int rollValue);

    /**
     * Display a move action performed by a player.
     * 
     * @param player the player performing the move
     * @param figure the figure being moved
     * @param steps the number of steps moved (0 if moving out of house)
     */
    void displayMove(Player player, GameFigure figure, int steps);

    /**
     * Display the winner of the game.
     * 
     * @param winner the player who won
     */
    void displayWinner(Player winner);

    /**
     * Display a general message to the user.
     * 
     * @param message the message to display
     */
    void displayMessage(String message);

    void displayPlayboard(Field[] fields, Player[] players);
}
