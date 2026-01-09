// uses GameManager.SCANNER for console input
/**
 * A keyboard-controlled player.
 * Prompts the user via console to choose a figure (1..numFigures).
 */
public class PlayerKeyboard extends Player {
    // no per-instance scanner: reuse shared GameManager.SCANNER

    /**
     * Create a keyboard player.
     *
     * @param name player name
     * @param numFigures number of figures this player owns
     * @param startField the starting field for this player's figures
     */
    public PlayerKeyboard(int id, String name, int numFigures, Field startField) {
        super(id ,name, numFigures, startField);
    }

    /**
     * Prompt the player to choose a figure from the given movable indices.
     * Displays which figures can be moved and asks the user to pick one.
     *
     * @param movableIndices array of 0-based figure indices that are movable (on board)
     * @return one of the indices from movableIndices
     */
    @Override
    protected int chooseFigure(GameFigure[] movableFigures) {
        if (movableFigures == null || movableFigures.length == 0) {
            System.out.println(this.getName() + " has no movable figures.");
            return -1;
        }

        // Map movable figures to their indices within this player's figure array
        int[] movableIndices = new int[movableFigures.length];
        GameFigure[] all = this.getFigures();
        for (int i = 0; i < movableFigures.length; i++) {
            GameFigure mf = movableFigures[i];
            int found = -1;
            for (int j = 0; j < all.length; j++) {
                if (all[j] == mf) { found = j; break; }
            }
            if (found == -1) throw new IllegalStateException("Movable figure not owned by player");
            movableIndices[i] = found;
        }

        System.out.print(this.getName() + ", choose a figure (");
        for (int i = 0; i < movableIndices.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(movableIndices[i] + 1); // display 1-based
        }
        System.out.println("):");
        for (int i = 0; i < movableIndices.length; i++) {
            System.out.println((movableIndices[i] + 1) + " Figure " + (movableIndices[i] + 1));
        }

        while (true) {
            System.out.print("> ");
            String line = GameManager.SCANNER.nextLine();
            try {
                int choice = Integer.parseInt(line.trim());
                for (int idx : movableIndices) {
                    if (choice == idx + 1) return idx; // return 0-based index
                }
                System.out.print("Invalid choice. Please choose from: ");
                for (int i = 0; i < movableIndices.length; i++) {
                    if (i > 0) System.out.print(", ");
                    System.out.print(movableIndices[i] + 1);
                }
                System.out.println();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
