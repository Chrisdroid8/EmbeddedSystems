public class GameFigure {
    private final Field house; // house position (starting position)
    private final Player owner; // Player who owns this figure
    private Field field; // current position, null when off-board
    private int id;

    public GameFigure(Player owner,int id) {
        this.id = id;
        this.owner = owner;
        this.house = new Field(-1, FieldType.HOUSE);
        this.house.setNext(this.owner.getStartField());
        this.field = this.house;
    }

    public Field getHouseField(){
        return house;
    }

    public int getId() {
        return id;
    }

    public void prepareForNewGame() {
        this.setField(house);
    }

    public Player getOwner() {
        return this.owner;
    }

    /** Returns the Field this figure stands on (house fields are returned as their Field with type HOUSE). */
    public Field getField() {
        return this.field;
    }

    /**
     * Set the field this figure stands on.
     * If already on a field, removes it first. Then adds to the new field.
     *
     * @param newField the field to move to (may be {@code null})
     */
    public void setField(Field newField) {
        //if (this.field == newField) return;
        // remove from old field
        this.field.removeFigure(this);
        this.field = newField;
        this.field.addFigure(this);
    }

    public void moveToHouse() {
        this.setField(this.house);
    }

    public void moveOutOfHouse() {
        this.setField(this.owner.getStartField());
    }

    /** Clear used by Field when removing occupant. */
    public void clearField() {
        // Called when a field clears its occupants: move this figure back to its house
        this.field = this.house;
    }

    /** Move this figure forward by the given number of fields. This will update both sides (old and new fields). */
    public void move(int numSteps) {
        if (this.field.isHouse()) {
            throw new IllegalStateException("Figure is in house and cannot move");
        }
        Field newField;
        // get the field in numSteps ahead
        newField = this.field.getDestination(numSteps, true);
        
        // remove from old field (clear occupants on this single figure)
        if (!this.field.isHouse()) {
            this.field.removeFigure(this);
        }
        this.field = newField;
        // set on new field if it exists and is not a house
        if (!this.field.isHouse()) {
            this.field.addFigure(this);
        }
    }
}
