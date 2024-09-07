package il.ac.sce.elctronicvoting.consoleapplication;


import il.ac.sce.elctronicvoting.projectclasses.vote.VoteService;
import il.ac.sce.elctronicvoting.projectclasses.vote.VotingResult;

import java.io.BufferedReader;
import java.util.concurrent.atomic.AtomicReference;

class ResultHandlerAccount extends Account {
    private static final String ACCOUNT_NAME = "Result Handler account:";
    private static final String NEWLINE = "\n";
    private static final String MENU_POINT = "Menu Point : ";
    private static final String MENU_POINT_ONE = "1";
    private static final String MENU_POINT_ZERO = "0";
    private static final String MENU_HEAD = "Please, enter:";
    private static final String WRONG_POINT = "A wrong menu point was entered\n";
    private static final String MENU_VOTING_RESULT = "  " + MENU_POINT_ONE + " - Voting Result Details";
    private static final String MENU_EXIT = "  " + MENU_POINT_ZERO + " - Exit";
    private static final String EXIT = " Exiting from account ...";
    private static final String RESULT_NO_VOTES = "The Vote List is empty.";
    private static final String RESULT_HEAD = "Voting results:";
    private static final String RESULT_BODY_CENTER = "  Center : ";
    private static final String RESULT_BODY_REPUBLICAN = "  Republican votes : ";
    private static final String RESULT_BODY_DEMOCRAT = "  Democrat votes : ";
    private static final String RESULT_FOOTER = "Total Voting result:";
    private static final String RESULT_FOOTER_AMOUNT_VOTERS = "  Total amount voters : ";
    private static final String RESULT_FOOTER_APPROPRIATE_VOTES = "  Total amount appropriate votes : ";
    private static final String RESULT_FOOTER_CORRUPTED_VOTES = "  Amount of corrupted votes : ";
    private static final String RESULT_FOOTER_REPUBLICAN = "  Total amount of Republican votes : ";
    private static final String RESULT_FOOTER_DEMOCRAT = "  Total amount of Democrat votes : ";
    private static final String RESULT_FOOTER_WINNER = "Winner : ";
    private static final String RESULT_FOOTER_NO_WINNER = "Democrat and Republican parties got the same amount of votes";

    private final VoteService voteService;
    private final BufferedReader reader;

    ResultHandlerAccount(BufferedReader reader) {
        this.voteService = new VoteService();
        this.reader = reader;
    }

    @Override
    void goToAccount() throws Exception {
        System.out.println(ACCOUNT_NAME);

        account : while (true) {
            System.out.print(getMenu());
            String point = reader.readLine();

            switch (point) {
                case MENU_POINT_ONE: {
                    printResult();
                } break;

                case MENU_POINT_ZERO: {
                    System.out.println(EXIT);
                } break account;

                default: {
                    System.out.println(WRONG_POINT);
                } break;
            }
        }
    }

    private void printResult() throws Exception {
        VotingResult result = voteService.getVotingResult();
        if (result.getTotalAmountVotes() == 0) {
            System.out.println(RESULT_NO_VOTES);
        } else {
            printFullResult(result);
        }
    }

    private void printFullResult(VotingResult result) {
        int amountCenters = result.getRepublicanVotes().length;
        System.out.println(RESULT_HEAD);

        for (int center = 1; center <= amountCenters; center++) {
            int republicanVotes = result.getRepublicanVotes()[center - 1];
            int democratVotes = result.getDemocratVotes()[center - 1];

            System.out.println(getCenterResultString(center, republicanVotes, democratVotes));
        }

        System.out.println(getVotingDetails(result));
    }

    private String getVotingDetails(VotingResult result) {
        AtomicReference<StringBuilder> resultTotal = new AtomicReference<>(new StringBuilder());
        resultTotal.get()
                .append(NEWLINE)
                .append(RESULT_FOOTER)
                .append(NEWLINE)
                .append(RESULT_FOOTER_AMOUNT_VOTERS)
                .append(result.getTotalAmountVoters())
                .append(NEWLINE)
                .append(RESULT_FOOTER_APPROPRIATE_VOTES)
                .append(result.getTotalAmountVotes())
                .append(NEWLINE)
                .append(RESULT_FOOTER_CORRUPTED_VOTES)
                .append(result.getAmountCorruptedVotes())
                .append(NEWLINE)
                .append(RESULT_FOOTER_REPUBLICAN)
                .append(result.getTotalAmountRepublican())
                .append(NEWLINE)
                .append(RESULT_FOOTER_DEMOCRAT)
                .append(result.getTotalAmountDemocrat())
                .append(NEWLINE);

        if (result.getWinner() == null) {
            resultTotal.get().append(RESULT_FOOTER_NO_WINNER).append(NEWLINE);
        } else {
            resultTotal.get().append(RESULT_FOOTER_WINNER).append(result.getWinner().getName()).append(NEWLINE);
        }

        return resultTotal.toString();
    }

    private String getCenterResultString(int center, int republicanVotes, int democratVotes) {
        AtomicReference<StringBuilder> resultCenter = new AtomicReference<>(new StringBuilder());
        resultCenter.get()
                .append(NEWLINE)
                .append(RESULT_BODY_CENTER)
                .append(center)
                .append(NEWLINE)
                .append(RESULT_BODY_REPUBLICAN)
                .append(republicanVotes)
                .append(NEWLINE)
                .append(RESULT_BODY_DEMOCRAT)
                .append(democratVotes)
                .append(NEWLINE);

        return resultCenter.toString();
    }

    private String getMenu() {
        AtomicReference<StringBuilder> menu = new AtomicReference<>(new StringBuilder());
        menu.get()
                .append(MENU_HEAD)
                .append(NEWLINE)
                .append(MENU_VOTING_RESULT)
                .append(NEWLINE)
                .append(MENU_EXIT)
                .append(NEWLINE)
                .append(MENU_POINT);

        return menu.toString();
    }
}
