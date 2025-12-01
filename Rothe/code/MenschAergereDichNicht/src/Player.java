public class Player {
    private final String name;
    private final GameFigure[] figures;
    private final Field startField;
    private final int numFigures;

    public Player(String name, int numFigures, Field startField) {
        this.name = name;
        this.startField = startField;
        this.numFigures = numFigures;
        this.figures = new GameFigure[this.numFigures];
        for (int i = 0; i < this.numFigures; i++) {
            this.figures[i] = new GameFigure(this);
        }
    }

    public String getName() {
        return name;
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
