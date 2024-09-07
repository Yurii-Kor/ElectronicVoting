package il.ac.sce.elctronicvoting.projectclasses.voter;

import java.util.concurrent.atomic.AtomicReference;

public class Voter {
    private static final String NEWLINE = "\n";
    private static final String INFO_HEAD  = "Voter info:";
    private static final String INFO_BODY_ID  = "   ID : ";
    private static final String INFO_BODY_NAME  = " Name : ";

    private String id;
    private String center;
    private String name;
    private String password;

    Voter (String id, String center, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.center = center;
    }

    @Override
    public String toString() {
        AtomicReference<StringBuilder> info = new AtomicReference<>(new StringBuilder());
        info.get().append(NEWLINE)
                .append(INFO_HEAD)
                .append(NEWLINE)
                .append(INFO_BODY_ID)
                .append(id)
                .append(NEWLINE)
                .append(INFO_BODY_NAME)
                .append(name)
                .append(NEWLINE);

        return info.toString();
    }

    public String getId() {
        return id;
    }
    public String getCenter() {
        return center;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }

    void setId(String id) {
        this.id = id;
    }
    public void setCenter(String center) {
        this.center = center;
    }
    void setName(String name) {
        this.name = name;
    }
    void setPassword(String password) {
        this.password = password;
    }
}

