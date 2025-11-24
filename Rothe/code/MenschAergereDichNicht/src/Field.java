public class Field {
    private final Occupation occupation;
    private Field next; // reference to the next field in the board (for circular board)

    public Field() {
        this.occupation = new Occupation();
        this.next = null;
    }

    public boolean isOccupied() {
        return this.occupation.isOccupied();
    }

    public GameFigure getOccupant() {
        return this.occupation.getGameFigure();
    }

    /**
     * Get the next field in sequence (may be null until GameManager links fields).
     */
    public Field getNext() {
        return this.next;
    }

    /**
     * Returns the field that is numSteps ahead of this field.
     * If numSteps is 0 returns this field. If the chain is not fully linked
     * and a null is encountered while advancing, null is returned.
     */
    public Field getNext(int numSteps) {
        if (numSteps < 0) throw new IllegalArgumentException("numSteps must be >= 0");
        Field current = this;
        for (int i = 0; i < numSteps; i++) {
            if (current == null) return null;
            current = current.next;
        }
        return current;
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
        GameFigure current = this.occupation.getGameFigure();
        if (current == figure) return;

        // if there's a previous occupant, move it to the next field (or clear it)
        if (current != null) {
            // delegate relocation to the GameFigure API: place it on the next field
            current.setToHouse();
        }

        this.occupation.setGameFigure(figure);
        this.occupation.setPlayer(figure == null ? null : figure.getOwner());

        if (figure != null && figure.getField() != this) {
            figure.setField(this);
        }
    }

    /**
     * Remove any occupant from this field.
     * Notify the GameFigre to delete its Field by Default
     */
    public void clearOccupant() {
        this.clearOccupant(true);
    }
    
    /** Remove any occupant from this field. */
    public void clearOccupant(boolean notifyFigure) {
        GameFigure current = this.occupation.getGameFigure();
        if (current != null && notifyFigure) {
            current.clearField();
        }
        this.occupation.clear();
    }
}
