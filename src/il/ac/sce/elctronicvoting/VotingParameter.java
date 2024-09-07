package il.ac.sce.elctronicvoting;

public enum VotingParameter {
    AMOUNT_VOTERS(10),
    AMOUNT_CENTERS(3),
    DEFAULT_ID(1_000_000);

    private final int value;

    VotingParameter(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
