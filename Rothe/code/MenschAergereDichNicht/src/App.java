public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Demo: GameManager & relations");

        GameManager manager = new GameManager();

        Player p0 = manager.getPlayer(0);
        GameFigure figuresOfP0[] = p0.getFigures();
        Field fields[] = manager.getFields();

        fields[0].setOccupant(figuresOfP0[0]);
        fields[0].setOccupant(figuresOfP0[1]);
        figuresOfP0[2].setField(fields[0]);


        // ---------------------- PRINTS ----------------------

    }
}