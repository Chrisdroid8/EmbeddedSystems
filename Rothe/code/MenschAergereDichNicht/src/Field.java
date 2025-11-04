public class Field {
    private GameFigure occupant;
    private Field next; // reference to the next field in the board (for circular board)

    public Field() {
        this.occupant = null;
        this.next = null;
    }

    public boolean isOccupied() {
        return this.occupant != null;
    }

    public GameFigure getOccupant() {
        return this.occupant;
    }

    /**
     * Get the next field in sequence (may be null until GameManager links fields).
     */
    public Field getNext() {
        return this.next;
    }

    /**
     * Set the next field in sequence. Intended to be used by GameManager when building the board.
     */
    public void setNext(Field next) {
        this.next = next;
    }

    /**
     * Place a figure on this field. This will update the figure's internal field reference.
     * If there is an existing occupant it will be replaced (caller may want to handle that).
     */
    public void setOccupant(GameFigure figure) {
        if (this.occupant == figure) return;
        // detach previous occupant if any
        if (this.occupant != null) {
            this.occupant.moveTo(this.next);
        }
        this.occupant = figure;
        if (this.occupant != null && this.occupant.getField() != this) {
            this.occupant.setField(this);
        }
    }

    /** Remove any occupant from this field. */
    public void clearOccupant() {
        if (this.occupant != null) {
            this.occupant.clearField();
            this.occupant = null;
        }
    }
}
