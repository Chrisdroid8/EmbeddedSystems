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

    /** Setter used by Field to avoid recursion. */
    public void setField(Field newField) {
        if (this.field == newField) return; // Figure is already on this field
        if (this.field != null) {
            this.field.clearOccupant();
        }
        this.field = newField;
        if (this.field != null && this.field.getOccupant() != this) {
            this.field.setOccupant(this);
        }
    }

    public void setToHouse() {
        this.setField(this.house);
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
        Field newField;
        newField = this.field.getNext(numSteps);
        // remove from old field
        this.field.clearOccupant(false);
        this.field = newField;
        // set on new field if it exists (it has to exist, if it doesn't there is a programming error)
        if (this.field != null) {
            this.field.setOccupant(this);
        }
    }
}
