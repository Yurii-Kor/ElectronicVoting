package il.ac.sce.elctronicvoting.projectclasses.vote;

import java.util.concurrent.atomic.AtomicReference;

public class Vote {
    private static final String NEWLINE = "\n";
    private static final String INFO_HEAD  = "Your Vote:";
    private static final String INFO_BODY_CENTER  = " Center : ";
    private static final String INFO_BODY_CHOICE  = " Choice : ";

    private String id;
    private String center;
    private String choice;

    Vote (String id, String center, String choice) {
        this.id = id;
        this.center = center;
        this.choice = choice;
    }

    @Override
    public String toString() {
        AtomicReference<StringBuilder> info = new AtomicReference<>(new StringBuilder());
        info.get()
                .append(INFO_HEAD)
                .append(NEWLINE)
                .append(INFO_BODY_CENTER)
                .append(center)
                .append(NEWLINE)
                .append(INFO_BODY_CHOICE)
                .append(choice)
                .append(NEWLINE);

        return info.toString();
    }

    public String getId() {
        return id;
    }
    public String getCenter() {
        return center;
    }
    public String getChoice() {
        return choice;
    }

    void setCenter(String center) {
        this.center = center;
    }
    void setChoice(String choice) {
        this.choice = choice;
    }
}

