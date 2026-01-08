public class GameManager {
    private final Field[] fields;
    private final Player[] players;
    private final I_RuleSet ruleSet;
    private final I_Visual visual;
    private static final int PLAYER_COUNT_MAX = 4;
    private static final int FIGURES_PER_PLAYER_MAX = 16;
    private final int playerCount; // actual number of players chosen at runtime
    // Shared scanner for all interactive console input. Do not close directly; closed via shutdown hook.
    public static final java.util.Scanner SCANNER = new java.util.Scanner(System.in);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                SCANNER.close();
            } catch (Exception ignored) {
            }
        }));
    }

    public GameManager() {
        // ask user for desired number of players (bounded by PLAYER_COUNT_MAX)
        this.playerCount = this.initialPlayersInput();
        // ask user how many figures per player
        int figuresPerPlayer = this.initialFiguresInput();
        this.visual = new VisualASCII();
        this.ruleSet = new RuleSetStandard(this.playerCount);

        //int numFields = this.ruleSet.getNumFields();
        int numFields = this.ruleSet.getNumFields();
        this.fields = new Field[numFields];
        // Precompute start indices so we can assign START type while creating fields
        int[] startIndices = new int[this.playerCount];
        for (int p = 0; p < this.playerCount; p++) {
            startIndices[p] = p * (fields.length / this.playerCount);
        }
        for (int i = 0; i < fields.length; i++) {
            boolean isStart = false;
            for (int s : startIndices) if (s == i) { isStart = true; break; }
            fields[i] = new Field(i, isStart ? FieldType.START : FieldType.NORMAL);
        }

        // Link fields into a circular list: each field's next points to the following field,
        // and the last field points back to the first.
        for (int i = 0; i < fields.length; i++) {
            Field next = fields[(i + 1) % fields.length];
            fields[i].setNext(next);
        }
        this.players = new Player[this.playerCount];
        for (int p = 0; p < players.length; p++) {
            if (fields.length % players.length != 0) {
                throw new IllegalArgumentException("Fields cannot be equally distributed among players");
            }
            int startIndex = p * (fields.length / players.length);
            players[p] = new PlayerKeyboard("Player " + (p + 1), figuresPerPlayer, fields[startIndex]);
        }
        resetGame();
        runGame();
    }

    public void runGame() { // ToDo
        visual.displayMessage("Game Started!");
        visual.displayGameState(fields, players);
        
        boolean gameWon = false;
        Player winner = null;
        int currentPlayerIndex = 0;
        
        // Main game loop
        while (!gameWon) {
            ruleSet.resetLastAction();
            Player currentPlayer = players[currentPlayerIndex];
            visual.displayCurrentPlayer(currentPlayer);
            
            boolean turnComplete = false;
            
            // Keep rolling until the turn is complete
            while (!turnComplete) {
                // Check if player can roll
                if (!ruleSet.checkRoll(currentPlayer)) {
                    visual.displayMessage(currentPlayer.getName() + " cannot roll anymore.");
                    turnComplete = true;
                    break;
                }
                
                // Roll the die
                int rollValue = currentPlayer.roll();
                visual.displayRoll(currentPlayer, rollValue);
                
                // Check which figures can move
                java.util.List<GameFigure> movableFigures = ruleSet.checkMove(currentPlayer, rollValue);
                
                if (movableFigures.isEmpty()) {
                    visual.displayMessage(currentPlayer.getName() + " has no movable figures with this roll.");
                    // Continue rolling if allowed (checkRoll will handle the 3-roll limit)
                    continue;
                }
                
                // Player chooses a figure to move
                GameFigure[] movableArray = movableFigures.toArray(new GameFigure[0]);
                int chosenFigureIndex = currentPlayer.chooseFigure(movableArray);
                
                if (chosenFigureIndex < 0 || chosenFigureIndex >= currentPlayer.getFigures().length) {
                    // This should never happen if chooseFigure is implemented correctly
                    visual.displayMessage("Invalid figure choice.");
                    turnComplete = true;
                    break;
                }
                
                GameFigure chosenFigure = currentPlayer.getFigures()[chosenFigureIndex];
                
                // Move the figure
                if (chosenFigure.getField().isHouse()) {
                    chosenFigure.moveOutOfHouse();
                    visual.displayMove(currentPlayer, chosenFigure, 0);
                } else {
                    chosenFigure.move(rollValue);
                    visual.displayMove(currentPlayer, chosenFigure, rollValue);
                }
                
                // Display updated game state
                visual.displayGameState(fields, players);
                
                // After a successful move, the turn is complete
                turnComplete = true;
            }
            
            // Check for win
            if (ruleSet.checkWin(currentPlayer)) {
                gameWon = true;
                winner = currentPlayer;
            } else {
                // Next player's turn
                currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
            }
        }
        
        visual.displayWinner(winner);
    }

    private void resetGame() {
        for (Field field : fields) {
            field.clearOccupant();
        }
        // Reset all figures to their houses
        for (Player player : players) {
            for (GameFigure figure : player.getFigures()) {
                figure.prepareForNewGame();
            }
        }
    }

    private int initialPlayersInput() {
        int numPlayers = UserInput.readIntInRange("Enter number of players (2-" + PLAYER_COUNT_MAX + "): ", 2, PLAYER_COUNT_MAX, SCANNER);
        System.out.println("Using " + numPlayers + " player.");
        return numPlayers;
    }

    private int initialFiguresInput() {
        int numFigures = UserInput.readIntInRange("Enter number of figures per player (1-" + FIGURES_PER_PLAYER_MAX + "): ", 1, FIGURES_PER_PLAYER_MAX, SCANNER);
        System.out.println("Using " + numFigures + " figure(s) per player.");
        return numFigures;
    }

    public Field[] getFields() {
        return fields;
    }

    public Player[] getPlayers() {
        return players;
    }

    public I_RuleSet getRuleSet() {
        return ruleSet;
    }
}
