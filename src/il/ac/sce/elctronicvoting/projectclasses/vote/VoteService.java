package il.ac.sce.elctronicvoting.projectclasses.vote;

import il.ac.sce.elctronicvoting.projectclasses.voter.Voter;

public class VoteService {
    public void castVote(Voter currentVoter, String choice) throws Exception {
        new VoteCastor().castVote(currentVoter, choice);
    }

    public Vote authenticateVote(Voter currentVoter) throws Exception {
        return new VoteAuthenticator().obtainVote(currentVoter);
    }

    public VotingResult getVotingResult() throws Exception {
        return new VotingResultCalculator().calculateResult();
    }
}
