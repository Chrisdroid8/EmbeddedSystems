public class GameManager {
    private final Field[] fields;
    private final Player[] players;
    private final I_RuleSet ruleSet;
    private static final int PLAYER_COUNT = 4;

    public GameManager() {
        this.ruleSet = new RuleSetStandard(PLAYER_COUNT);
        int numFields = this.ruleSet.getNumFields();
        this.fields = new Field[numFields];
        // Precompute start indices so we can assign START type while creating fields
        int[] startIndices = new int[PLAYER_COUNT];
        for (int p = 0; p < PLAYER_COUNT; p++) {
            startIndices[p] = p * (fields.length / PLAYER_COUNT);
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
        this.players = new Player[PLAYER_COUNT];
        for (int p = 0; p < players.length; p++) {
            if (fields.length % players.length != 0) {
                throw new IllegalArgumentException("Fields cannot be equally distributed among players");
            }
            int startIndex = p * (fields.length / players.length);
            players[p] = new PlayerKeyboard("Player " + (p + 1), 4, fields[startIndex]);
        }
        resetGame();
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
