public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Demo: GameManager & relations");

        GameManager manager = new GameManager();

        Player p0 = manager.getPlayer(0);
        GameFigure figure0 = p0.getFigure(0);
        GameFigure figure1 = p0.getFigure(1);
        GameFigure figure2 = p0.getFigure(2);
        Field field0 = manager.getField(0);

        field0.setOccupant(figure0);
        field0.setOccupant(figure1);
        figure2.setField(field0);


        // ---------------------- PRINTS ----------------------
        // Print all Field states
        System.out.println("--- Fields ---");
        for (int i = 0; i < manager.getFields().length; i++) {
            Field f = manager.getField(i);
            String occ = f.isOccupied() ? ("Figure " + f.getOccupant().getId() + " of " + f.getOccupant().getOwner().getName()) : "empty";
            int nextIdx = -1;
            for (int j = 0; j < manager.getFields().length; j++) {
                if (manager.getField(j) == f.getNext()) nextIdx = j;
            }
            System.out.printf("Field %2d: %s | next: %d\n", i, occ, nextIdx);
        }

        // Print all Player states
        System.out.println("--- Players ---");
        for (int i = 0; i < manager.getPlayers().length; i++) {
            Player p = manager.getPlayer(i);
            System.out.println("Player " + i + " (" + p.getName() + "):");
            for (GameFigure fig : p.getFigures()) {
                String pos = "off-board";
                for (int j = 0; j < manager.getFields().length; j++) {
                    if (manager.getField(j) == fig.getField()) pos = "Field " + j;
                }
                System.out.printf("  Figure %d: %s\n", fig.getId(), pos);
            }
        }

        // Print all GameFigure states
        System.out.println("--- Figures ---");
        for (int i = 0; i < manager.getPlayers().length; i++) {
            Player p = manager.getPlayer(i);
            for (GameFigure fig : p.getFigures()) {
                String pos = "off-board";
                for (int j = 0; j < manager.getFields().length; j++) {
                    if (manager.getField(j) == fig.getField()) pos = "Field " + j;
                }
                System.out.printf("Figure %d of %s: %s\n", fig.getId(), p.getName(), pos);
            }
        }
    }
}