package il.ac.sce.elctronicvoting;

import il.ac.sce.elctronicvoting.consoleapplication.VotingApplication;

public class Main {
    private static final String EXIT = " Exiting from application ...";

    public static void main(String[] args) throws Exception {
        // id : 1000001   : example :
        // pass : 1 = id - default id = 1_000_001 - 1_000_000
        // center : 2 = pass % amount centers + 1 = 1 % 3 + 1
        new VotingApplication().startVoting();
        System.out.println(EXIT);
    }
}
