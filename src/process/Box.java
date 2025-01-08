package process;

public enum Box {
    EMPTY("🔵"),
    SHIP("🚢"),
    DEFEATED("❌️"),
    MISSED("⚪");

    private final String rep;

    Box(String rep) {
        this.rep = rep;
    }

    public String getRep() {
        return rep;
    }
}
