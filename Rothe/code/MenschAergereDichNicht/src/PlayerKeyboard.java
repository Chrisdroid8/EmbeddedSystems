import java.util.Scanner;

/**
 * A keyboard-controlled player.
 * Prompts the user via console to choose a figure (1..numFigures).
 */
public class PlayerKeyboard extends Player {
    private final Scanner scanner;

    /**
     * Create a keyboard player.
     *
     * @param name player name
     * @param numFigures number of figures this player owns
     * @param startField the starting field for this player's figures
     */
    public PlayerKeyboard(String name, int numFigures, Field startField) {
        super(name, numFigures, startField);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prompt the player to choose a figure from the given movable indices.
     * Displays which figures can be moved and asks the user to pick one.
     *
     * @param movableIndices array of 0-based figure indices that are movable (on board)
     * @return one of the indices from movableIndices
     */
    @Override
    public int chooseFigure(int[] movableIndices) {
        if (movableIndices.length == 0) {
            System.out.println(this.getName() + " has no movable figures.");
            return -1;
        }

        System.out.print(this.getName() + ", choose a figure (");
        for (int i = 0; i < movableIndices.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(movableIndices[i] + 1); // Show 1-based figure numbers
        }
        System.out.println("):");
        for (int i = 0; i < movableIndices.length; i++) {
            System.out.println((movableIndices[i] + 1) + " Figure " + (movableIndices[i] + 1));
        }

        while (true) {
            System.out.print("> ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                // Check if choice is one of the movable figure indices (1-based input)
                for (int idx : movableIndices) {
                    if (choice == idx + 1) { // idx + 1 because we want 1-based input
                        return idx; // Return 0-based index
                    }
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
