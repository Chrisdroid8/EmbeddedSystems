/**
 * Standard rule set implementation.
 * Defines rules for a typical "Mensch ärgere dich nicht" game.
 */
import java.util.ArrayList;
import java.util.List;
public class RuleSetStandard implements I_RuleSet {
    private static final int NUM_FIELDS_PER_PLAYER = 5;
    private static final int MAX_ROLLS_ALL_IN_HOUSE = 3;
    private final int playerCount;
    private ActionType lastActionType = ActionType.NONE;
    private int rollsThisTurn = 0;

    public RuleSetStandard(int playerCount) {
        if (playerCount <= 0) throw new IllegalArgumentException("playerCount must be positive");
        this.playerCount = playerCount;
    }

    @Override
    public boolean checkRoll(Player player) {
        // A player cannot roll if a figure was moved in the last action
        if (this.lastActionType == ActionType.MOVE) {
            this.rollsThisTurn = 0; // Reset rolls counter for next turn
            return false;
        }
        
        // Check if all figures are in house
        boolean allInHouse = true;
        for (GameFigure figure : player.getFigures()) {
            if (!figure.getField().isHouse()) {
                allInHouse = false;
                break;
            }
        }
        
        // If all figures are in house, allow up to 3 rolls
        if (allInHouse) {
            if (rollsThisTurn >= MAX_ROLLS_ALL_IN_HOUSE) {
                this.rollsThisTurn = 0; // Reset for next turn
                return false;
            }
            rollsThisTurn++;
            lastActionType = ActionType.ROLL;
            return true;
        }
        
        // A player may roll if they have at least one figure not in the goal
        for (GameFigure figure : player.getFigures()) {
            if (!figure.getField().isGoal()) {
                rollsThisTurn = 1; // Regular roll
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
            Field fieldStartOfMove = figure.getField();
            
            // Skip figures that are already in goal
            if (fieldStartOfMove.isGoal()) {
                continue;
            }
            
            // Figures in house can only move out with a roll of 6
            if (fieldStartOfMove.isHouse()) {
                if (rollValue == 6) {
                    movableFigures.add(figure);
                }
                continue;
            }

            // Check destination field for occupancy
            Field fieldDestination = fieldStartOfMove.getDestination(rollValue, true);
            if(fieldDestination.isOccupied()) {
                // Check if destination is occupied by someone else
                // This means a player cannot place more than 1 figure on the same field
                // but can kick opponents' figures
                if (fieldDestination.getOccupant().getOwner() != figure.getOwner()){
                    movableFigures.add(figure);
                    // continue; // Unnecessary
                }
            } else {
                // Destination is free
                movableFigures.add(figure);
            }
        }
        
        if (!movableFigures.isEmpty()) {
            lastActionType = ActionType.MOVE;
            this.rollsThisTurn = 0; // Reset rolls counter after a move
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
        if (playerCount >= 1 && playerCount <= 4){
            return 40;
        }
        else if (playerCount >= 5 && playerCount <= 6){
            return 8*6;
        }
        else{
            throw new IllegalArgumentException("Ungültige Spieleranzahl");        
        }
    }

    @Override
    public void resetLastAction(){
        this.lastActionType = ActionType.NONE;
    }
}
