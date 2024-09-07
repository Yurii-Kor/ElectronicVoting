package il.ac.sce.elctronicvoting.projectclasses.vote;

import il.ac.sce.elctronicvoting.Party;
import il.ac.sce.elctronicvoting.VotingParameter;
import il.ac.sce.elctronicvoting.projectclasses.encryption.EncryptionAES;
import il.ac.sce.elctronicvoting.projectclasses.voter.VoterService;

import javax.crypto.SecretKey;
import java.io.IOException;

class VotingResultCalculator {
    private final EncryptionAES encryptor;
    private final SecretKey key;
    private final VoteDB managerDB;
    private final VoterService service;
    private final VoteChecker checker;

    VotingResultCalculator () throws IOException {
        this.encryptor = new EncryptionAES();
        this.key = encryptor.loadKeyFromFile();
        this.managerDB = new VoteDB();
        this.service = new VoterService();
        this.checker = new VoteChecker();
    }

    VotingResult calculateResult() throws Exception {
        String[] idLisl = service.getHashedIdArray();
        Vote[] encryptedVotes = managerDB.getAllVotesFromDB();

        int amountCenters = VotingParameter.AMOUNT_CENTERS.getValue();
        int[] republicanVotes = new int[amountCenters];
        int[] democratVotes = new int[amountCenters];
        for (int i = 0; i < amountCenters; i++) {
            republicanVotes[i] = 0;
            democratVotes[i] = 0;
        }

        int amountCorruptedVotes = 0;
        int totalAmountVotes = 0;
        int totalAmountRepublican = 0;
        int totalAmountDemocrat = 0;

        for (Vote vote : encryptedVotes) {
            if (isVoteAppropriate(vote, idLisl)) {
                int center = Integer.parseInt(encryptor.decrypt(vote.getCenter(), key)) - 1;
                String choice = encryptor.decrypt(vote.getChoice(), key);

                if (Party.REPUBLICAN.getName().contains(choice)) {
                    republicanVotes[center] += 1;
                    totalAmountRepublican += 1;
                } else {
                    democratVotes[center] += 1;
                    totalAmountDemocrat += 1;
                }

                totalAmountVotes +=1;
            } else {
                amountCorruptedVotes += 1;
            }
        }

        VotingResult result = new VotingResult();
        result.setTotalAmountVoters(idLisl.length);
        result.setAmountCorruptedVotes(amountCorruptedVotes);
        result.setRepublicanVotes(republicanVotes);
        result.setDemocratVotes(democratVotes);
        result.setTotalAmountVotes(totalAmountVotes);
        result.setTotalAmountRepublican(totalAmountRepublican);
        result.setTotalAmountDemocrat(totalAmountDemocrat);
        setWinner(result);

        return result;
    }

    private void setWinner(VotingResult result) {
        result.setWinner(null);

        if (result.getTotalAmountRepublican() > result.getTotalAmountDemocrat()) {
            result.setWinner(Party.REPUBLICAN);
        }

        if (result.getTotalAmountRepublican() < result.getTotalAmountDemocrat()) {
            result.setWinner(Party.DEMOCRAT);
        }
    }

    private boolean isVoteAppropriate(Vote currentVote, String[] hashedIdList) {
        boolean isHashedIdCorrect = checker.isHashedIdCorrect(currentVote.getId(), hashedIdList);
        boolean isEncryptedCenterCorrect = checker.isEncryptedCenterCorrect(currentVote.getCenter());
        boolean isEncryptedChoiceCorrect = checker.isEncryptedChoiceCorrect(currentVote.getChoice());

        return isHashedIdCorrect && isEncryptedCenterCorrect && isEncryptedChoiceCorrect;
    }
}
