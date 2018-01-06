package victor.tarasov.model.trip;

public enum Locale {
    POLAND("pl_PL"), CROATIA("hr_HR");

    private String name;

    Locale(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
