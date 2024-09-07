package il.ac.sce.elctronicvoting;

public enum ResultHandler {
    ID("0"),
    PASSWORD("0");

    private final String value;

    ResultHandler(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
