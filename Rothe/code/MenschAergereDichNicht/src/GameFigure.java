public class GameFigure {
    private final Player owner;
    private Field field; // current position, null when off-board
    private final int id;

    public GameFigure(Player owner, int id) {
        this.owner = owner;
        this.id = id;
        this.field = null;
    }

    public Player getOwner() {
        return this.owner;
    }

    public int getId() {
        return this.id;
    }

    /** Returns the Field this figure stands on (may be null). */
    public Field getField() {
        return this.field;
    }

    /** Internal setter used by Field to avoid recursion. */
    void setField(Field f) {
        if (this.field == f) return;
        if (this.field != null) {
            this.field.clearOccupant();
        }
        this.field = f;
        if (this.field != null && this.field.getOccupant() != this) {
            this.field.setOccupant(this);
        }
    }

    /** Internal clear used by Field when removing occupant. */
    void clearField() {
        this.field = null;
    }

    /** Move this figure to the given field. This will update both sides (old and new fields).
     * If newField is null the figure will be removed from the board.
     */
    public void moveTo(Field newField) {
        if (this.field != null) {
            // remove from old field
            Field old = this.field;
            // clear occupant without touching this.field (Field.clearOccupant calls clearFieldInternal)
            old.clearOccupant();
        }
        if (newField != null) {
            newField.setOccupant(this);
        }
    }
}
