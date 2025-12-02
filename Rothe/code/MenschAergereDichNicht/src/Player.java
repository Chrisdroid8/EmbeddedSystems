/**
 * Abstract base class representing a player in the game.
 * Each player owns figures, a starting field, and a die (I_Rollable).
 * Subclasses implement specific player behavior (e.g., keyboard input, AI).
 */
public abstract class Player {
    private final String name;
    private final GameFigure[] figures;
    private final Field startField;
    private final int numFigures;
    private final I_Rollable die;

    /**
     * Create a player with the given name, number of figures, and starting field.
     * The player receives a standard 6-sided die.
     * This constructor is protected for use by subclasses only.
     *
     * @param name player name
     * @param numFigures number of figures this player owns
     * @param startField the starting field for this player's figures
     */
    protected Player(String name, int numFigures, Field startField) {
        this.name = name;
        this.startField = startField;
        this.numFigures = numFigures;
        this.figures = new GameFigure[this.numFigures];
        for (int i = 0; i < this.numFigures; i++) {
            this.figures[i] = new GameFigure(this);
        }
        this.die = new Die6();
    }

    /**
     * Let the player choose a figure from a list of movable figures.
     * Finds figures on the board (not in house), then calls the abstract
     * chooseFigure(int[]) to let the subclass decide.
     *
     * @param gameManager the game manager providing access to fields
     * @return index of the chosen figure (0-based) that is on the board, or -1 if no figures on board
     */
    public int chooseMovableFigure() {
        // Find indices of figures that are on the board (not in house)
        java.util.List<Integer> movableFigureIndices = new java.util.ArrayList<>();
        for (int i = 0; i < this.figures.length; i++) {
            Field f = this.figures[i].getField();
            if (!f.isHouse()) {
                movableFigureIndices.add(i);
            }
        }

        // If no figures on board, return -1
        if (movableFigureIndices.isEmpty()) {
            System.out.println(this.getName() + " has no figures on the board.");
            return -1;
        }

        // Convert list to array and let the subclass choose
        int[] movableIndices = new int[movableFigureIndices.size()];
        for (int i = 0; i < movableFigureIndices.size(); i++) {
            movableIndices[i] = movableFigureIndices.get(i);
        }

        return chooseFigure(movableIndices);
    }

    /**
     * Abstract method for subclass to choose a figure from the given movable indices.
     *
     * @param movableIndices array of 0-based figure indices that are movable
     * @return one of the indices from movableIndices array
     */
    protected abstract int chooseFigure(int[] movableIndices);

    public String getName() {
        return name;
    }

    public int roll() {
        return die.roll();
    }

    public GameFigure[] getFigures() {
        return figures;
    }

    public Field getStartField() {
        return startField;
    }

    public int getFiguresInHouse() {
        int count = 0;
        for (GameFigure figure : this.figures) {
            Field f = figure.getField();
            if (f.isHouse()) {
                count++;
            }
        }
        return count;
    }

    public void moveFigureOutOfHouse() {
        for (GameFigure figure : this.figures) {
            Field f = figure.getField();
            if (f.isHouse()) {
                figure.setField(this.startField);
                return;
            }
        }
    }
}
