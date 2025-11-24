/**
 * Occupation holds an optional player and the occupying game figure for a Field.
 * This is the existing richer representation used by the project.
 */
public class Occupation {
    private Player player;
    private GameFigure gameFigure;

    public Occupation() {
        this.player = null;
        this.gameFigure = null;
    }

    public Occupation(Player player, GameFigure gameFigure) {
        this.player = player;
        this.gameFigure = gameFigure;
    }

    public boolean isOccupied() {
        return this.gameFigure != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameFigure getGameFigure() {
        return gameFigure;
    }

    public void setGameFigure(GameFigure gameFigure) {
        this.gameFigure = gameFigure;
    }

    /** Clear both player and figure references. */
    public void clear() {
        this.player = null;
        this.gameFigure = null;
    }
}
