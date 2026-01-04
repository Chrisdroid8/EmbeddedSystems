public class GameManager {
    private final Field[] fields;
    private final Player[] players;
    private final I_RuleSet ruleSet;
    private static final int PLAYER_COUNT_MAX = 10;
    private static final int FIGURES_PER_PLAYER_MAX = 20;
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
        this.ruleSet = new RuleSetStandard(this.playerCount);
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
    }

    public void runGame() {
        
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
        System.out.println("Using " + numPlayers + " player(s).");
        return numPlayers;
    }

    private int initialFiguresInput() {
        int numFigures = UserInput.readIntInRange("Enter number of figures per player (1-" + FIGURES_PER_PLAYER_MAX + "): ", 1, FIGURES_PER_PLAYER_MAX, SCANNER);
        System.out.println("Using " + numFigures + " figures per player.");
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
