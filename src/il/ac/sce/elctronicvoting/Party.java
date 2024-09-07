package il.ac.sce.elctronicvoting;

public enum Party {
    DEMOCRAT("Democrat"),
    REPUBLICAN("Republican");

    private final String name;

    Party(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
