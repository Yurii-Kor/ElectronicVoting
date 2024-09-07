package il.ac.sce.elctronicvoting.projectclasses.vote;

import il.ac.sce.elctronicvoting.Party;

public class VotingResult {
    private int totalAmountVoters;
    private int amountCorruptedVotes;
    private int[] republicanVotes;
    private int[] democratVotes;
    private int totalAmountVotes;
    private int totalAmountDemocrat;
    private int totalAmountRepublican;
    private Party winner;

    VotingResult() {}

    public int getTotalAmountVoters() {
        return totalAmountVoters;
    }

    public int getAmountCorruptedVotes() {
        return amountCorruptedVotes;
    }

    public int[] getRepublicanVotes() {
        return republicanVotes;
    }

    public int[] getDemocratVotes() {
        return democratVotes;
    }

    public int getTotalAmountVotes() {
        return totalAmountVotes;
    }

    public int getTotalAmountDemocrat() {
        return totalAmountDemocrat;
    }

    public int getTotalAmountRepublican() {
        return totalAmountRepublican;
    }

    public Party getWinner() {
        return winner;
    }

    void setTotalAmountVoters(int totalAmountVoters) {
        this.totalAmountVoters = totalAmountVoters;
    }


    void setAmountCorruptedVotes(int amountCorruptedVotes) {
        this.amountCorruptedVotes = amountCorruptedVotes;
    }

    void setRepublicanVotes(int[] republicanVotes) {
        this.republicanVotes = republicanVotes;
    }

    void setDemocratVotes(int[] democratVotes) {
        this.democratVotes = democratVotes;
    }

    void setTotalAmountVotes(int totalAmountVotes) {
        this.totalAmountVotes = totalAmountVotes;
    }

    void setTotalAmountRepublican(int totalAmountRepublican) {
        this.totalAmountRepublican = totalAmountRepublican;
    }

    public void setTotalAmountDemocrat(int totalAmountDemocrat) {
        this.totalAmountDemocrat = totalAmountDemocrat;
    }

    void setWinner(Party winner) {
        this.winner = winner;
    }
}
