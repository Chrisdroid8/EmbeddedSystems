/**
 * A computer-controlled player.
 * Randomly chooses a figure from the available movable figures.
 */
public class PlayerPC extends Player {

    /**
     * Create a computer player.
     *
     * @param id player ID
     * @param name player name
     * @param numFigures number of figures this player owns
     * @param startField the starting field for this player's figures
     */
    public PlayerPC(int id, String name, int numFigures, Field startField) {
        super(id, name, numFigures, startField);
    }

    /**
     * Randomly choose a figure from the given movable figures.
     *
     * @param movableFigures array of figures that are movable
     * @return a random index from the movable figures
     */
    @Override
    protected int chooseFigure(GameFigure[] movableFigures) {
        if (movableFigures == null || movableFigures.length == 0) {
            System.out.println(this.getName() + " has no movable figures.");
            return -1;
        }

        // Map movable figures to their indices within this player's figure array
        GameFigure[] all = this.getFigures();
        int randomIndex = (int) (Math.random() * movableFigures.length);
        GameFigure chosenFigure = movableFigures[randomIndex];
        
        // Find the index of the chosen figure in the player's figure array
        for (int j = 0; j < all.length; j++) {
            if (all[j] == chosenFigure) {
                // System.out.println(this.getName() + " (PC) chose figure " + (j + 1));
                return j;
            }
        }
        
        throw new IllegalStateException("Movable figure not owned by player");
    }
}
