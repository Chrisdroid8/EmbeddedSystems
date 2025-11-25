/**
 * Represents a single board field.
 * Fields form a circular sequence via `next` references. Each Field may hold an
 * `Occupation` describing which player's figure stands on it.
 */
public class Field {
    private final Occupation occupation;
    private Field next; // reference to the next field in the board (for circular board)
    private final int index; // index of the field for easier identification

    /**
     * Create a Field with the given index. Use indices 0..n-1 for regular board fields.
     * Special fields (e.g. houses) can use negative indices.
     *
     * @param index integer index identifying the field
     */
    public Field(int index) {
        this.index = index;
        this.occupation = new Occupation();
        this.next = null;
    }

    /**
     * Return true if this field currently has an occupying figure.
     *
     * @return {@code true} when a figure occupies this field, {@code false} otherwise
     */
    public boolean isOccupied() {
        return this.occupation.isOccupied();
    }

    /**
     * Get the occupying figure for this field, or {@code null} when empty.
     *
     * @return occupying {@link GameFigure} or {@code null}
     */
    public GameFigure getOccupant() {
        return this.occupation.getGameFigure();
    }

    /**
     * Get the next field in sequence (may be {@code null} until GameManager links fields).
     *
     * @return the next {@link Field} in the board sequence, or {@code null}
     */
    public Field getNext() {
        return this.next;
    }

    /**
     * Get this field's index as assigned by the {@link GameManager}.
     * For regular board fields this will be a non-negative index (0..N-1).
     * Special fields (houses) may use negative indices.
     *
     * @return integer index identifying this field
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the field that is {@code numSteps} ahead of this field.
     * If {@code numSteps} is 0 returns this field. If the chain is not fully linked
     * and a {@code null} is encountered while advancing, {@code null} is returned.
     *
     * @param numSteps non-negative number of steps to advance
     * @return the field {@code numSteps} ahead or {@code null} if unavailable
     * @throws IllegalArgumentException when {@code numSteps} is negative
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
     * Set the next field in sequence. Intended to be used by {@link GameManager} when building the board.
     *
     * @param next the next Field in sequence
     */
    public void setNext(Field next) {
        this.next = next;
    }

    /**
     * Place a figure on this field. This will update the figure's internal field reference.
     * If there is an existing occupant it will be replaced (caller may want to handle that).
     *
     * @param figure the {@link GameFigure} to place on this field (may be {@code null} to clear)
     */
    public void setOccupant(GameFigure figure) {
        GameFigure current = this.occupation.getGameFigure();
        if (current == figure) return;

        // if there's a previous occupant, move it to its house
        if (current != null) {
            current.setToHouse();
        }

        this.occupation.setGameFigure(figure);
        this.occupation.setPlayer(figure == null ? null : figure.getOwner());

        if (figure != null && figure.getField() != this) {
            figure.setField(this);
        }
    }

    /**
     * Remove any occupant from this field. Notifies the occupying figure (if any) by default.
     */
    public void clearOccupant() {
        this.clearOccupant(true);
    }
    
    /**
     * Remove any occupant from this field.
     *
     * @param notifyFigure if {@code true} will notify the occupying Fig to clear its reference
     */
    public void clearOccupant(boolean notifyFigure) {
        GameFigure current = this.occupation.getGameFigure();
        if (current != null && notifyFigure) {
            current.clearField();
        }
        this.occupation.clear();
    }

    /**
     * Return a short debug string describing this field (index and occupant).
     *
     * @return debug string
     */
    @Override
    public String toString() {
        GameFigure g = this.getOccupant();
        if (g == null) return "Field(" + index + "): <empty>";
        return "Field(" + index + "): " + g.getOwner().getName();
    }
}

