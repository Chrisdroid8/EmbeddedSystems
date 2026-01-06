/**
 * Standard rule set implementation.
 * Defines rules for a typical "Mensch ärgere dich nicht" game.
 */
import java.util.ArrayList;
import java.util.List;
public class RuleSetStandard implements I_RuleSet {
    private static final int NUM_FIELDS_PER_PLAYER = 5;
    private final int playerCount;
    private ActionType lastActionType = ActionType.NONE;

    public RuleSetStandard(int playerCount) {
        if (playerCount <= 0) throw new IllegalArgumentException("playerCount must be positive");
        this.playerCount = playerCount;
    }

    @Override
    public boolean checkRoll(Player player) {
        // A player cannot roll if a figure was moved in the last action
        if (this.lastActionType == ActionType.MOVE) {
            return false;
        }
        // A player may roll if they have at least one figure not in the goal
        for (GameFigure figure : player.getFigures()) {
            if (!figure.getField().isGoal()) {
                lastActionType = ActionType.ROLL;
                return true;
            }
        }
        return false;
    }

    @Override
    public List<GameFigure> checkMove(Player player, int rollValue) {
        List<GameFigure> movableFigures = new ArrayList<>();
        if (this.lastActionType != ActionType.ROLL) {
            return movableFigures; // No move possible if last action was not a roll
        }
        for (GameFigure figure : player.getFigures()) {
            if (!figure.getField().isGoal()) {
                movableFigures.add(figure);
            }
        }
        if (!movableFigures.isEmpty()) {
            lastActionType = ActionType.MOVE;
        }
        return movableFigures;
    }

    @Override
    public boolean checkWin(Player player) {
        for (GameFigure figure : player.getFigures()) {
            if (!figure.getField().isGoal()) {
                return false;
            }
        }
        lastActionType = ActionType.WIN;
        return true;
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS_PER_PLAYER * playerCount;
    }
}
