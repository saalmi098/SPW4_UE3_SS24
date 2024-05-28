package spw4.connectfour;

public enum Player {
    none(".", "NONE"),
    yellow("Y", "YELLOW"),
    red("R" , "RED");

    private final String symbol;
    private final String name;

    Player(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
