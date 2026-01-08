/**
 * Abstract base class representing a player in the game.
 * Each player owns figures, a starting field, and a die (I_Rollable).
 * Subclasses implement specific player behavior (e.g., keyboard input, AI).
 */
public abstract class Player {
    private final String name;
    private final GameFigure[] figures;
    private final Field startField;
    private final Field[] goalFields;
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
        // Create goal fields for this player: one goal field per figure
        this.goalFields = new Field[this.numFigures];
        for (int i = 0; i < this.numFigures; i++) {
            // Use negative indices for player-specific special fields
            this.goalFields[i] = new Field(-100 - i, FieldType.GOAL);
            if (i<this.numFigures-1){
                this.goalFields[i].setNext(this.goalFields[i+1]);
            }
        }
        this.die = new Die6();
    }

    /**
     * Abstract method for subclass to choose a figure from the given movable indices.
     *
     * @param movableFigures array of figures that are movable
     * @return one of the indices from movableFigures
     */
    protected abstract int chooseFigure(GameFigure[] movableFigures);

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

    /**
     * Get the goal fields belonging to this player.
     * There is exactly one goal field per figure owned by the player.
     *
     * @return array of goal {@link Field} objects
     */
    public Field[] getGoalFields() {
        return this.goalFields;
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

    public Field[] getHouseFields(){
         Field[] houseFields = new Field[this.figures.length];

        for (int i = 0; i < this.figures.length; i++) {
            houseFields[i] = this.figures[i].getHouseField();
        }

        return houseFields;
    }
}
