package il.ac.sce.elctronicvoting.projectclasses.vote;

import il.ac.sce.elctronicvoting.Party;
import il.ac.sce.elctronicvoting.projectclasses.encryption.EncryptionAES;
import il.ac.sce.elctronicvoting.projectclasses.encryption.HashingSHA;
import il.ac.sce.elctronicvoting.projectclasses.voter.Voter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class VoteAuthenticator {
    private static final String CORRUPTED_VOTE_DATA = "Corrupted vote detected and will be deleted: ";
    private static final String OBTAINING_VOTE_ERROR = "An error occurred while obtaining the vote: ";
    private static final String DECRYPTION_ERROR_LOG = "Failed to decrypt vote  ";

    private static final Logger logger = Logger.getLogger(VoteAuthenticator.class.getName());

    private final HashingSHA hash;
    private final EncryptionAES encryptor;
    private final SecretKey key;
    private final VoteChecker checker;
    private final VoteDB managerDB;

    VoteAuthenticator() throws IOException {
        this.hash = new HashingSHA();
        this.encryptor = new EncryptionAES();
        this.key = encryptor.loadKeyFromFile();
        this.checker = new VoteChecker();
        this.managerDB = new VoteDB();
    }

    Vote obtainVote(Voter currentVoter) {
        try {
            Vote obtainedVote = managerDB.getVoteFromDB(hash.hash(currentVoter.getId()));
            if (obtainedVote == null) {
                return null;
            }

            boolean isCenterCorrect = checker.isEncryptedCenterCorrect(obtainedVote.getCenter());
            boolean isChoiceCorrect = checker.isEncryptedChoiceCorrect(obtainedVote.getChoice());

            if (isCenterCorrect && isChoiceCorrect) {
                decryptVote(obtainedVote);
                setFullPartyName(obtainedVote);
                return obtainedVote;
            } else {
                logger.log(Level.WARNING, CORRUPTED_VOTE_DATA + obtainedVote);
                managerDB.deleteIncorrectVoteFromDB(obtainedVote);
                return null;
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, OBTAINING_VOTE_ERROR + e.getMessage(), e);
            return null;
        }
    }

    private void decryptVote(Vote obtainedVote) {
        try {
            obtainedVote.setCenter(encryptor.decrypt(obtainedVote.getCenter(), key));
            obtainedVote.setChoice(encryptor.decrypt(obtainedVote.getChoice(), key));
        } catch (Exception e) {
            logger.log(Level.SEVERE, DECRYPTION_ERROR_LOG + e.getMessage(), e);
            throw new RuntimeException(DECRYPTION_ERROR_LOG, e);
        }
    }

    private void setFullPartyName (Vote vote) {
        String partyName = vote.getChoice();
        if (Party.DEMOCRAT.getName().contains(partyName)) {
            vote.setChoice(Party.DEMOCRAT.getName());
        }
        if (Party.REPUBLICAN.getName().contains(partyName)) {
            vote.setChoice(Party.REPUBLICAN.getName());
        }
    }
}
