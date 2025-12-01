public class GameFigure {
    private final Field house; // house position (starting position)
    private final Player owner; // Player who owns this figure
    private Field field; // current position, null when off-board

    public GameFigure(Player owner) {
        this.owner = owner;
        this.house = new Field(-1);
        this.house.setNext(this.owner.getStartField());
        this.field = house;
    }

    public void prepareForNewGame() {
        this.setField(house);
    }

    public Player getOwner() {
        return this.owner;
    }

    /** Returns the Field this figure stands on (may be null). */
    public Field getField() {
        if (this.field == house) {
            return null;
        }
        return this.field;
    }

    /**
     * Set the field this figure stands on.
     * If already on a field, removes it first. Then adds to the new field.
     *
     * @param newField the field to move to (may be {@code null})
     */
    public void setField(Field newField) {
        if (this.field == newField) return;
        if (this.field != null) {
            this.field.removeFigure(this);
        }
        this.field = newField;
        if (this.field != null) {
            this.field.addFigure(this);
        }
    }

    public void moveToHouse() {
        this.setField(this.house);
    }

    public void moveOutOfHouse() {
        this.setField(this.owner.getStartField());
    }

    /** Clear used by Field when removing occupant. */
    public void clearField() {
        if (this.field == null) return;
        this.field.clearOccupant(false);
        this.field = null;
    }

    /** Move this figure forward by the given number of fields. This will update both sides (old and new fields). */
    public void move(int numSteps) {
        if(this.field == null) {
            throw new IllegalStateException("Figure is not on the board");
        }
        // get the field in numSteps ahead
        Field newField = this.field.getNext(numSteps);
        // remove from old field
        this.field.clearOccupant(false);
        this.field = newField;
        // set on new field if it exists (it has to exist, if it doesn't there is a programming error)
        if (this.field != null) {
            this.field.addFigure(this);
        }
    }
}
