package il.ac.sce.elctronicvoting.consoleapplication;

import il.ac.sce.elctronicvoting.Party;
import il.ac.sce.elctronicvoting.projectclasses.vote.Vote;
import il.ac.sce.elctronicvoting.projectclasses.vote.VoteService;
import il.ac.sce.elctronicvoting.projectclasses.voter.Voter;
import il.ac.sce.elctronicvoting.projectclasses.voter.VoterService;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

class VoterAccount extends Account {
    private static final String NEWLINE = "\n";
    private static final String MENU_POINT = "Menu Point : ";
    private static final String MENU_POINT_ONE = "1";
    private static final String MENU_POINT_TWO = "2";
    private static final String MENU_POINT_ZERO = "0";
    private static final String MENU_HEAD = "Please, enter:";
    private static final String MENU_SUBMIT_VOTE = "  " + MENU_POINT_ONE + " - Submit Vote";
    private static final String MENU_VOTE_INFO = "  " + MENU_POINT_ONE + " - Vote Details";
    private static final String MENU_EXIT = "  " + MENU_POINT_ZERO + " - Exit";
    private static final String ENTER_CENTER_VALUE = "Enter the Center Number or \"" + MENU_POINT_ZERO + "\" for exit";
    private static final String FIELD_CENTER = "Center : ";
    private static final String PARTY_CHOICE = "Choose the Party or enter \"" + MENU_POINT_ZERO + "\" to exit";
    private static final String MENU_POINT_REPUBLICAN = "  " + MENU_POINT_ONE + " - " + Party.REPUBLICAN.getName();
    private static final String MENU_POINT_DEMOCRAT = "  " + MENU_POINT_TWO + " - " + Party.DEMOCRAT.getName();
    private static final String FIELD_PARTY = "Party : ";
    private static final String VOTING_RESULT = " voted for : ";
    private static final String CANCEL_VOTING = " canceled voting\n";
    private static final String WRONG_POINT = "A wrong menu point was entered\n";
    private static final String WRONG_VALUE = "A wrong value was entered\n";
    private static final String EXIT = " Exiting from account ...";

    private final VoteService voteService;
    private final VoterService voterService;
    private final Voter currentVoter;
    private final BufferedReader reader;

    VoterAccount(Voter currentVoter, BufferedReader reader) {
        this.voteService = new VoteService();
        this.voterService = new VoterService();
        this.currentVoter = currentVoter;
        this.reader = reader;
    }

    @Override
    void goToAccount() throws Exception {
        System.out.println(currentVoter.toString());

        while (true) {
            Vote existedVote = voteService.authenticateVote(currentVoter);

            System.out.print(getMenu(existedVote));
            String enteredMenuPoint = reader.readLine();

            if (enteredMenuPoint.equals(MENU_POINT_ONE)) {
                if (existedVote != null) {
                    System.out.println(existedVote);
                } else {
                    submitVote(currentVoter, reader);
                }
            }

            else if (enteredMenuPoint.equals(MENU_POINT_ZERO)) {
                System.out.println(currentVoter.getName() + EXIT);
                break;
            }

            else {
                System.out.println(WRONG_POINT);
            }
        }
    }

    private String getMenu(Vote existedVote) {
        StringBuilder menu = new StringBuilder();
        menu.append(MENU_HEAD)
                .append(NEWLINE);

        if (existedVote == null) {
            menu.append(MENU_SUBMIT_VOTE)
                    .append(NEWLINE);
        } else {
            menu.append(MENU_VOTE_INFO)
                    .append(NEWLINE);
        }

        menu.append(MENU_EXIT)
                .append(NEWLINE)
                .append(MENU_POINT);

        return menu.toString();
    }

    private void submitVote(Voter currentVoter, BufferedReader reader) throws Exception {
        boolean isEnteredCenterCorrect = checkCenter(currentVoter, reader);
        if (isEnteredCenterCorrect) {
            castVote(currentVoter, reader);
        }
    }

    private boolean checkCenter(Voter currentVoter, BufferedReader reader) throws IOException, NoSuchAlgorithmException {
        while (true) {
            System.out.println(ENTER_CENTER_VALUE);
            System.out.print(FIELD_CENTER);
            String center = reader.readLine();

            if (center.equals(MENU_POINT_ZERO)) {
                return false;
            }

            boolean isCenterCorrect = voterService.authenticateVotingCenter(currentVoter, center);

            if (!isCenterCorrect) {
                System.out.println(WRONG_VALUE);
            } else {
                return true;
            }
        }
    }

    private void castVote(Voter currentVoter, BufferedReader reader) throws Exception {
        label : while (true) {
            System.out.print(getVotingMenu());

            String party = reader.readLine();

            switch (party) {
                case MENU_POINT_ONE: {
                    System.out.println(currentVoter.getName() + VOTING_RESULT + Party.REPUBLICAN.getName() + NEWLINE);
                    voteService.castVote(currentVoter, Party.REPUBLICAN.getName());
                    break label;
                }

                case MENU_POINT_TWO: {
                    System.out.println(currentVoter.getName() + VOTING_RESULT + Party.DEMOCRAT.getName() + NEWLINE);
                    voteService.castVote(currentVoter, Party.DEMOCRAT.getName());
                    break label;
                }

                case MENU_POINT_ZERO: {
                    System.out.println(currentVoter.getName() + CANCEL_VOTING);
                    break label;
                }

                default: {
                    System.out.println(WRONG_POINT);
                } break;
            }
        }
    }

    private String getVotingMenu() {
        StringBuilder menu;

        menu = new StringBuilder();
        menu.append(PARTY_CHOICE)
                .append(NEWLINE)
                .append(MENU_POINT_REPUBLICAN)
                .append(NEWLINE)
                .append(MENU_POINT_DEMOCRAT)
                .append(NEWLINE)
                .append(FIELD_PARTY);

        return menu.toString();
    }
}
