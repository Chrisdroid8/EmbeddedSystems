/**
 * Represents a single board field.
 * Fields form a circular sequence via `next` references. Each Field may hold an
 * `Occupation` describing which player's figure stands on it.
 */
public class Field {
    private final Occupation occupation;
    private Field next; // reference to the next field in the board (for circular board)
    private final int index; // index of the field for easier identification
    private final FieldType type;

    /**
     * Create a Field with the given index. Use indices 0..n-1 for regular board fields.
     * Special fields (e.g. houses) can use negative indices.
     *
     * @param index integer index identifying the field
     */
    public Field(int index) {
        this(index, FieldType.NORMAL);
    }

    /**
     * Create a Field with the given index and type.
     * @param index integer index identifying the field
     * @param type the {@link FieldType} of this field
     */
    public Field(int index, FieldType type) {
        if (type == null) throw new IllegalArgumentException("Field type must not be null");
        this.index = index;
        this.type = type;
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

    public FieldType getType() {
        return this.type;
    }

    public boolean isHouse() { return this.type == FieldType.HOUSE; }
    public boolean isStart() { return this.type == FieldType.START; }
    public boolean isGoal() { return this.type == FieldType.GOAL; }
    public boolean isNormal() { return this.type == FieldType.NORMAL; }

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
    public Field getDestination(int numSteps, boolean tryGoal) {
        if (numSteps < 0) throw new IllegalArgumentException("numSteps must be >= 0");
        if (numSteps == 0) return this;
        if (this.type == FieldType.HOUSE) {
            return this.occupation.getPlayer().getStartField();
        }

        Field stepCountingField = this;
        for (int i = 0; i < numSteps; i++) {
            // System.out.println("Count field " + stepCountingField.getIndex());
            if (tryGoal && stepCountingField.next == this.occupation.getPlayer().getStartField()) {
                stepCountingField = this.occupation.getPlayer().getGoalFields()[0];
            }
            else stepCountingField = stepCountingField.next; // boards are expected to be fully linked; nulls are not allowed
            if (stepCountingField == null) {
                if (!tryGoal) return this;
                return this.getDestination(numSteps, false);
            }
        }
        return stepCountingField;
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
     * Add a figure to this field. Multiple figures from the same player are allowed.
     *
     * @param figure the {@link GameFigure} to add
     * @throws IllegalArgumentException if different player's figures occupy this field
     */
    public void addFigure(GameFigure figure) {
        if (figure == null) throw new IllegalArgumentException("figure must not be null");
        if (!this.occupation.canAccept(figure.getOwner())) {
            for (GameFigure fig : this.occupation.getFigures()) {
                fig.moveToHouse();
            }
        }
        this.occupation.addFigure(figure);
        if (figure.getField() != this) {
            figure.setField(this);
        }
    }

    /**
     * Remove a specific figure from this field.
     *
     * @param figure the figure to remove
     */
    public void removeFigure(GameFigure figure) {
        this.occupation.removeFigure(figure);
    }

    /**
     * Remove any occupant from this field. Notifies the occupying figure (if any) by default.
     */
    public void clearOccupant() {
        this.clearOccupant(true);
    }
    
    /**
     * Remove all occupants from this field.
     *
     * @param notifyFigures if {@code true} will notify figures to clear their references
     */
    public void clearOccupant(boolean notifyFigures) {
        if (notifyFigures) {
            for (GameFigure fig : this.occupation.getFigures()) {
                fig.clearField();
            }
        }
        this.occupation.clear();
    }

    /**
     * Return a short debug string describing this field (index and occupants).
     *
     * @return debug string
     */
    @Override
    public String toString() {
        java.util.List<GameFigure> figs = this.occupation.getFigures();
        if (figs.isEmpty()) return "Field(" + index + "): <empty>";
        StringBuilder sb = new StringBuilder("Field(" + index + "): ");
        for (int i = 0; i < figs.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(figs.get(i).getOwner().getName());
        }
        return sb.toString();
    }
}

