/**
 * Represents a player in the game.
 * Each player owns figures, a starting field, and a die (I_Rollable).
 */
public class Player {
    private final String name;
    private final GameFigure[] figures;
    private final Field startField;
    private final int numFigures;
    private final I_Rollable die;

    /**
     * Create a player with the given name, number of figures, and starting field.
     * The player receives a standard 6-sided die.
     *
     * @param name player name
     * @param numFigures number of figures this player owns
     * @param startField the starting field for this player's figures
     */
    public Player(String name, int numFigures, Field startField) {
        this.name = name;
        this.startField = startField;
        this.numFigures = numFigures;
        this.figures = new GameFigure[this.numFigures];
        for (int i = 0; i < this.numFigures; i++) {
            this.figures[i] = new GameFigure(this);
        }
        this.die = new Die6();
    }

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
            if (figure.getField() == null) {
                count++;
            }
        }
        return count;
    }

    public void moveFigureOutOfHouse() {
        for (GameFigure figure : this.figures) {
            if (figure.getField() == null) {
                figure.setField(this.startField);
                return;
            }
        }
    }
}
