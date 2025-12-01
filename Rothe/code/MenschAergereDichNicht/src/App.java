public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Demo: GameManager & relations");

        GameManager manager = new GameManager();

        Player[] players = manager.getPlayers();
        // GameFigure[] figuresOfP0 = players[0].getFigures();
        // Field[] fields = manager.getFields();

        printGameState(manager);
        players[0].moveFigureOutOfHouse();
        printGameState(manager);
        players[1].moveFigureOutOfHouse();
        printGameState(manager);
        players[1].moveFigureOutOfHouse();
        printGameState(manager);

        players[0].getFigures()[0].move(players[0].roll());
        printGameState(manager);
        players[1].getFigures()[0].move(players[1].roll());
        printGameState(manager);
    }

    private static void printGameState(GameManager manager) {
        System.out.println("--- Game State ---");

        Player[] players = manager.getPlayers();
        Field[] fields = manager.getFields();

        for (int p = 0; p < players.length; p++) {
            GameFigure[] figs = players[p].getFigures();
            for (int f = 0; f < figs.length; f++) {
                GameFigure fig = figs[f];
                Field figField = fig.getField();
                int fieldIndex = -1;
                if (figField != null) {
                    for (int k = 0; k < fields.length; k++) {
                        if (fields[k] == figField) { fieldIndex = k; break; }
                    }
                }
                String pos;
                if (figField == null) {
                    pos = "home";
                } else if (fieldIndex >= 0) {
                    pos = String.valueOf(fieldIndex);
                } else {
                    pos = "none";
                }
                System.out.println("Player " + p + " Figure " + f + " is on field " + pos);
            }
        }

        System.out.println("--- Fields ---");

        for (int i = 0; i < fields.length; i++) {
            java.util.List<String> occupants = new java.util.ArrayList<>();
            for (int p = 0; p < players.length; p++) {
                GameFigure[] figs = players[p].getFigures();
                for (int f = 0; f < figs.length; f++) {
                    if (figs[f].getField() == fields[i]) {
                        occupants.add("P" + p + "F" + f);
                    }
                }
            }
            if (occupants.isEmpty()) {
                System.out.println("Field " + i + ": <empty>");
            } else {
                System.out.println("Field " + i + ": " + String.join(", ", occupants));
            }
        }
    }
}