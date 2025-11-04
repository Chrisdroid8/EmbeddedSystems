public class Player {
    private final String name;
    private final GameFigure[] figures;

    public Player(String name) {
        this.name = name;
        this.figures = new GameFigure[4];
        for (int i = 0; i < 4; i++) {
            this.figures[i] = new GameFigure(this, i);
        }
    }

    public String getName() {
        return name;
    }

    public GameFigure[] getFigures() {
        return figures;
    }

    public GameFigure getFigure(int index) {
        if (index < 0 || index >= figures.length) throw new IndexOutOfBoundsException();
        return figures[index];
    }
}
