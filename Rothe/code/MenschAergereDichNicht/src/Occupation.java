import java.util.ArrayList;
import java.util.List;

/**
 * Occupation holds an optional player and a list of occupying game figures from that player.
 * All figures on a field must belong to the same player.
 */
public class Occupation {
    private Player player;
    private final List<GameFigure> figures;

    public Occupation() {
        this.player = null;
        this.figures = new ArrayList<>();
    }

    public boolean isOccupied() {
        return !this.figures.isEmpty();
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameFigure> getFigures() {
        return new ArrayList<>(this.figures);
    }

    public GameFigure getGameFigure() {
        return this.figures.isEmpty() ? null : this.figures.get(0);
    }

    public void addFigure(GameFigure figure) {
        if (figure == null) throw new IllegalArgumentException("figure must not be null");
        if (!this.figures.isEmpty() && !this.player.equals(figure.getOwner())) {
            throw new IllegalArgumentException(
                "Cannot add figure from " + figure.getOwner().getName() +
                " to field occupied by " + this.player.getName()
            );
        }
        if (this.figures.isEmpty()) {
            this.player = figure.getOwner();
        }
        if (!this.figures.contains(figure)) {
            this.figures.add(figure);
        }
    }

    public void removeFigure(GameFigure figure) {
        this.figures.remove(figure);
        if (this.figures.isEmpty()) {
            this.player = null;
        }
    }

    public boolean canAccept(Player player) {
        return this.figures.isEmpty() || this.player.equals(player);
    }

    public void clear() {
        this.figures.clear();
        this.player = null;
    }
}
