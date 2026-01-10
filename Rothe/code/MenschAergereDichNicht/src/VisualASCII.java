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
            System.out.println("\n"+ this.getPlayerColor(player.getId()) + player.getName() +"\u001B[0m");
            
            GameFigure[] figures = player.getFigures();
            for (int f = 0; f < figures.length; f++) {
                Field figureField = figures[f].getField();
                String location = getFieldLocation(figureField, fields);
                System.out.printf("  Figure %d: %s%n", (f + 1), location);
            }
        }
        System.out.println(SEPARATOR + "\n");
    }
    
    public void displayPlayboard(Field[] fields, Player[] players){
        clearConsole();
        String[][] boardLegende = {
            {"H10", "H11", "-",   "-",   "F18", "F19", "F20", "-",   "-",   "H22", "H20"},
            {"H12", "H13", "-",   "-",   "F17", "G20", "F21", "-",   "-",   "H23", "H21"},
            {"-",   "-",   "-",   "-",   "F16", "G21", "F22", "-",   "-",   "-",   "-"  },
            {"-",   "-",   "-",   "-",   "F15", "G22", "F23", "-",   "-",   "-",   "-"  },
            {"F10", "F11", "F12", "F13", "F14", "G23", "F24", "F25", "F26", "F27", "F28"},
            {"F9",  "G10", "G11", "G12", "G13", "-",   "G33", "G32", "G31", "G30", "F29"},
            {"F8",  "F7",  "F6",  "F5",  "F4",  "G03", "F34", "F33", "F32", "F31", "F30"},
            {"-",   "-",   "-",   "-",   "F3",  "G02", "F35", "-",   "-",   "-",   "-"  },
            {"-",   "-",   "-",   "-",   "F2",  "G01", "F36", "-",   "-",   "-",   "-"  },
            {"H01", "H03", "H05",   "-",   "F1",  "G00", "F37", "-",   "-",   "H33", "H32"},
            {"H00", "H02", "H04",   "-",   "F0",  "F39", "F38", "-",   "-",   "H31", "H30"},
        };
        Field[][] mappedBoard = new Field[11][11]; // 2D-Array für das Board

        for (int row = 0; row < boardLegende.length; row++) {
            for (int col = 0; col < boardLegende[row].length; col++) {

                String cell = boardLegende[row][col];

                if (cell.equals("-")) {
                    mappedBoard[row][col] = null;
                    continue;
                }

                char typeChar = cell.charAt(0);
                int index = Integer.parseInt(cell.substring(1));

                switch (typeChar) {

                    case 'F': // normale Spielfelder
                        mappedBoard[row][col] = fields[index];
                        break;

                    case 'H': {
                        int playerId = Character.getNumericValue(cell.charAt(1));
                        if (playerId >= players.length){
                            break;
                        }
                        int homeIndex = Character.getNumericValue(cell.charAt(2));
                        if( homeIndex >= players[playerId].getFigures().length){
                            break;
                        }
                        mappedBoard[row][col] = players[playerId].getHouseField(homeIndex);
                        break;
                    }

                    case 'G': {
                        int playerId = Character.getNumericValue(cell.charAt(1));
                        if (playerId >= players.length){
                            break;
                        }
                        int goalIndex = Character.getNumericValue(cell.charAt(2));
                        if( goalIndex >= players[playerId].getFigures().length){
                            break;
                        }
                        mappedBoard[row][col] = players[playerId].getGoalFields()[goalIndex];
                        break;
                    }

                    default:
                        throw new IllegalStateException("Unbekannter Feldtyp: " + cell);
                }
            }
        }

        for (int row = 0; row < mappedBoard.length; row++) {
            for (int col = 0; col < mappedBoard[row].length; col++) {

                Field field = mappedBoard[row][col];

                if (field == null) {
                    System.out.print(" "); // leeres Feld
                } else {
                    switch (field.getType()) {
                        case NORMAL:
                            if (field.isOccupied()){
                                System.out.print(this.getPlayerColor(field.getOccupant().getOwner().getId()));
                                System.out.print(String.valueOf(field.getOccupant().getId()+1));
                            }else{
                                System.out.print(".");
                            }
                            break;
                        case HOUSE:
                            if (field.isOccupied()){
                                System.out.print(this.getPlayerColor(field.getOccupant().getOwner().getId()));
                                System.out.print(String.valueOf(field.getOccupant().getId()+1));
                            }else{
                                System.out.print(this.getPlayerColor(Character.getNumericValue(boardLegende[row][col].charAt(1))));
                                System.out.print(".");  
                            }                  
                            break;
                        case GOAL:
                            if (field.isOccupied()){
                                System.out.print(this.getPlayerColor(field.getOccupant().getOwner().getId()));
                                System.out.print(String.valueOf(field.getOccupant().getId()+1));
                            }else{
                                System.out.print(this.getPlayerColor(Character.getNumericValue(boardLegende[row][col].charAt(1))));
                                System.out.print(".");  
                            }                  
                            break;
                        case START:
                            if (field.isOccupied()){
                                System.out.print(this.getPlayerColor(field.getOccupant().getOwner().getId()));
                                System.out.print(String.valueOf(field.getOccupant().getId()+1));
                            }else{
                                System.out.print(this.getPlayerColor(Character.getNumericValue(boardLegende[row][col].charAt(1))));
                                System.out.print(".");  
                            }                  
                            break;
                        default:
                            System.out.print("?");
                    }
                    System.out.print("\u001B[0m");
                }

                System.out.print(" "); // Abstand zwischen Feldern
            }
            System.out.println(); // neue Zeile nach jeder Board-Zeile
        }

    }
    

    @Override
    public void displayCurrentPlayer(Player player) {
        System.out.println("\n" + LINE);
        System.out.println(">>> " + this.getPlayerColor(player.getId())+ player.getName() + "'s Turn \u001B[0m <<<");
        System.out.println(LINE);
    }

    @Override
    public void displayRoll(Player player, int rollValue) {
        String diceArt = getDiceArt(rollValue);
        System.out.println("\n" + this.getPlayerColor(player.getId())+ player.getName() + " rolls: \u001B[0m");
        //System.out.println(diceArt);
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
            System.out.print(player.getName() + " moves Figure " + figureIndex + " OUT OF HOUSE");
        } else {
            System.out.print(player.getName() + " moves Figure " + figureIndex + " by " + steps + " step(s)");
        }
    }

    @Override
    public void displayWinner(Player winner) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("          🏆 GAME OVER 🏆");
        System.out.println(SEPARATOR);
        System.out.println("\n   *** " + this.getPlayerColor(winner.getId())+ winner.getName() + " WINS!\u001B[0m ***\n");
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
        String occString = "";
        // Check all players' figures to see if any are on this field
        for (Player player : players) {
            GameFigure[] figures = player.getFigures();
            int i = 0;
            for (GameFigure figure : figures) {
                if (figure.getField() == field) {
                    int playerIndex = getPlayerIndex(player, players);
                    occString = getPlayerColor(playerIndex) + String.valueOf(i) +"\u001B[0m"+" ";
                }
                i++;
            }
        }
        
        if (occString.length() == 0) {
            return "Empty";
        }
        return occString;
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
    // private String getPlayerSymbol(int playerIndex) {
    //     char[] symbols = {'●', '■', '▲', '◆', '★', '♠', '♣', '♥', '♦', '☺'};
    //     //String[] color = {"\u001B[31m", "\u001B[32m","\u001B[33m","\u001B[34m","\u001B[35m","\u001B[36m",};
    //     //String[] symbols = {"\u001B[31mo\u001B[0m", "\u001B[32mo\u001B[0m","\u001B[33mo\u001B[0m","\u001B[34mo\u001B[0m","\u001B[35mo\u001B[0m","\u001B[36mo\u001B[0m",};
    //     if (playerIndex >= 0 && playerIndex < symbols.length) {
    //         return String.valueOf(symbols[playerIndex]);
    //     }
    //     return "?";
    // }

    private String getPlayerColor(int playerIndex){
        String[] colors = {"\u001B[31m", "\u001B[32m","\u001B[33m","\u001B[34m","\u001B[35m","\u001B[36m",};
        if (playerIndex >= 0 && playerIndex < colors.length) {
            return String.valueOf(colors[playerIndex]);
        }
        return "?";
    }

    private static void clearConsole() {
        System.out.print("\u001B[2J\u001B[H");
        System.out.flush();
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
