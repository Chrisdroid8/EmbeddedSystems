/**
 * ASCII-based visualization for the game.
 * Displays game state using ASCII characters in the terminal.
 */
public class VisualASCII implements I_Visual {
    private static final String SEPARATOR = "=====================================";
    private static final String LINE = "-------------------------------------";

    @Override
    public void displayGameState(Field[] fields, Player[] players) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("          GAME STATE");
        System.out.println(SEPARATOR);
        
        // Display board fields
        System.out.println("\nBoard Fields:");
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldType = getFieldTypeSymbol(field);
            String occupants = getOccupantsString(field, players);
            
            System.out.printf("[%2d] %s %s%n", i, fieldType, occupants);
        }
        
        // Display players and their figures
        System.out.println("\n" + LINE);
        System.out.println("Players:");
        for (int p = 0; p < players.length; p++) {
            Player player = players[p];
            System.out.println("\n" + player.getName() + " (Symbol: " + getPlayerSymbol(p) + ")");
            
            GameFigure[] figures = player.getFigures();
            for (int f = 0; f < figures.length; f++) {
                Field figureField = figures[f].getField();
                String location = getFieldLocation(figureField, fields);
                System.out.printf("  Figure %d: %s%n", (f + 1), location);
            }
        }
        System.out.println(SEPARATOR + "\n");
    }

    @Override
    public void displayCurrentPlayer(Player player) {
        System.out.println("\n" + LINE);
        System.out.println(">>> " + player.getName() + "'s Turn <<<");
        System.out.println(LINE);
    }

    @Override
    public void displayRoll(Player player, int rollValue) {
        String diceArt = getDiceArt(rollValue);
        System.out.println("\n" + player.getName() + " rolls:");
        System.out.println(diceArt);
        System.out.println("Result: " + rollValue);
    }

    @Override
    public void displayMove(Player player, GameFigure figure, int steps) {
        int figureIndex = -1;
        GameFigure[] figures = player.getFigures();
        for (int i = 0; i < figures.length; i++) {
            if (figures[i] == figure) {
                figureIndex = i + 1;
                break;
            }
        }
        
        if (steps == 0) {
            System.out.println("→ " + player.getName() + " moves Figure " + figureIndex + " OUT OF HOUSE");
        } else {
            System.out.println("→ " + player.getName() + " moves Figure " + figureIndex + " by " + steps + " steps");
        }
    }

    @Override
    public void displayWinner(Player winner) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("          🏆 GAME OVER 🏆");
        System.out.println(SEPARATOR);
        System.out.println("\n   *** " + winner.getName() + " WINS! ***\n");
        System.out.println(SEPARATOR + "\n");
    }

    @Override
    public void displayMessage(String message) {
        System.out.println("• " + message);
    }

    /**
     * Get symbol representing field type.
     */
    private String getFieldTypeSymbol(Field field) {
        if (field.isHouse()) {
            return "[H]"; // House
        } else if (field.isGoal()) {
            return "[G]"; // Goal
        } else if (field.getType() == FieldType.START) {
            return "[S]"; // Start
        } else {
            return "[ ]"; // Normal field
        }
    }

    /**
     * Get string representation of occupants on a field.
     */
    private String getOccupantsString(Field field, Player[] players) {
        StringBuilder sb = new StringBuilder();
        
        // Check all players' figures to see if any are on this field
        for (Player player : players) {
            GameFigure[] figures = player.getFigures();
            for (GameFigure figure : figures) {
                if (figure.getField() == field) {
                    int playerIndex = getPlayerIndex(player, players);
                    sb.append(getPlayerSymbol(playerIndex)).append(" ");
                }
            }
        }
        
        if (sb.length() == 0) {
            return "Empty";
        }
        return sb.toString().trim();
    }

    /**
     * Get player index in the players array.
     */
    private int getPlayerIndex(Player player, Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get symbol for a player based on their index.
     */
    private String getPlayerSymbol(int playerIndex) {
        char[] symbols = {'●', '■', '▲', '◆', '★', '♠', '♣', '♥', '♦', '☺'};
        if (playerIndex >= 0 && playerIndex < symbols.length) {
            return String.valueOf(symbols[playerIndex]);
        }
        return "?";
    }

    /**
     * Get location description of a field.
     */
    private String getFieldLocation(Field field, Field[] fields) {
        if (field.isHouse()) {
            return "In House";
        } else if (field.isGoal()) {
            return "In Goal";
        } else {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i] == field) {
                    return "On Field " + i;
                }
            }
            return "Unknown";
        }
    }

    /**
     * Get ASCII art representation of a die face.
     */
    private String getDiceArt(int value) {
        switch (value) {
            case 1:
                return " -----\n" +
                       "|     |\n" +
                       "|  ●  |\n" +
                       "|     |\n" +
                       " -----";
            case 2:
                return " -----\n" +
                       "| ●   |\n" +
                       "|     |\n" +
                       "|   ● |\n" +
                       " -----";
            case 3:
                return " -----\n" +
                       "| ●   |\n" +
                       "|  ●  |\n" +
                       "|   ● |\n" +
                       " -----";
            case 4:
                return " -----\n" +
                       "| ● ● |\n" +
                       "|     |\n" +
                       "| ● ● |\n" +
                       " -----";
            case 5:
                return " -----\n" +
                       "| ● ● |\n" +
                       "|  ●  |\n" +
                       "| ● ● |\n" +
                       " -----";
            case 6:
                return " -----\n" +
                       "| ● ● |\n" +
                       "| ● ● |\n" +
                       "| ● ● |\n" +
                       " -----";
            default:
                return " -----\n" +
                       "|     |\n" +
                       "|  ?  |\n" +
                       "|     |\n" +
                       " -----";
        }
    }
}
