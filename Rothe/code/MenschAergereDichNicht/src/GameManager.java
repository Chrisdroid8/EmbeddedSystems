public class GameManager {
    private final Field[] fields;
    private final Player[] players;

    public GameManager() {
        this.fields = new Field[20];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Field();
        }

        // Link fields into a circular list: each field's next points to the following field,
        // and the last field points back to the first.
        for (int i = 0; i < fields.length; i++) {
            Field next = fields[(i + 1) % fields.length];
            fields[i].setNext(next);
        }

        this.players = new Player[4];
        for (int p = 0; p < players.length; p++) {
            players[p] = new Player("Player " + (p + 1));
        }
    }

    public Field[] getFields() {
        return fields;
    }

    public Field getField(int index) {
        if (index < 0 || index >= fields.length) throw new IndexOutOfBoundsException();
        return fields[index];
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        if (index < 0 || index >= players.length) throw new IndexOutOfBoundsException();
        return players[index];
    }
}
