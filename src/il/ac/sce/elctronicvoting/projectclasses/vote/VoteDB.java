package il.ac.sce.elctronicvoting.projectclasses.vote;

import il.ac.sce.elctronicvoting.projectclasses.DBConnection;

import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;

class VoteDB {
    private static final String TABLE_NAME = "votes";
    private static final String FIELD_ID = "id";
    private static final String FIELD_CENTER = "center";
    private static final String FIELD_CHOICE = "choice";
    private static final String COMMA = ", ";
    private static final String APOSTROPHE = "'";
    private static final String REQUEST_INSERT_HEAD = "INSERT INTO " + TABLE_NAME + " (id, center, choice) VALUES (";
    private static final String REQUEST_INSERT_FOOTER = ")";
    private static final String REQUEST_SELECT_ALL_VOTES = "SELECT * FROM " + TABLE_NAME;
    private static final String REQUEST_SELECT_ONE_VOTE = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String REQUEST_COUNT = "SELECT COUNT(*) FROM " + TABLE_NAME;
    private static final String REQUEST_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id LIKE ";

    void deleteIncorrectVoteFromDB(Vote incorrectVote) {
        try (Connection connection = DBConnection.getDBConnection();
             Statement statement = connection.createStatement()) {

            String deleteRequest = getDeleteRequest(incorrectVote);
            statement.executeQuery(deleteRequest);

        } catch (SQLException | ClassNotFoundException ignored) {}
    }

    int getAmountOfVotesFromDB() {
        try (Connection connection = DBConnection.getDBConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(REQUEST_COUNT)) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ignored) {}

        return 0;
    }

    Vote[] getAllVotesFromDB() {
        int amountVotes = getAmountOfVotesFromDB();

        String[] valuesId = new String[amountVotes];
        String[] valuesCenter = new String[amountVotes];
        String[] valuesChoice = new String[amountVotes];

        try (Connection connection = DBConnection.getDBConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(REQUEST_SELECT_ALL_VOTES)) {

            int index = 0;
            while (resultSet.next() && index < amountVotes) {
                valuesId[index] = resultSet.getString(FIELD_ID);
                valuesCenter[index] = resultSet.getString(FIELD_CENTER);
                valuesChoice[index] = resultSet.getString(FIELD_CHOICE);

                index++;
            }
        } catch (SQLException | ClassNotFoundException ignored) {
            return new Vote[0];
        }

        Vote[] votes = new Vote[amountVotes];
        for (int i = 0; i < amountVotes; i++) {
            votes[i] = new Vote(valuesId[i], valuesCenter[i], valuesChoice[i]);
        }

        return votes;
    }

    Vote getVoteFromDB(String hashedVoterId) {
        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement stmt = connection.prepareStatement(REQUEST_SELECT_ONE_VOTE)) {

            stmt.setString(1, hashedVoterId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String storedId = resultSet.getString(FIELD_ID);
                String storedCenter = resultSet.getString(FIELD_CENTER);
                String storedChoice = resultSet.getString(FIELD_CHOICE);

                return new Vote(storedId, storedCenter, storedChoice);
            }
        } catch (SQLException | ClassNotFoundException ignored) {}

        return null;
    }
    void addVoteToDB (Vote encryptedVote) {
        try (Connection connection = DBConnection.getDBConnection();
             Statement statement = connection.createStatement()) {

            String insertRequest = getInsertRequest(encryptedVote);
            statement.executeQuery(insertRequest);

        } catch (SQLException | ClassNotFoundException ignored) {}
    }

    private String getInsertRequest(Vote encryptedVote) {
        AtomicReference<StringBuilder> insertRequest = new AtomicReference<>(new StringBuilder());
        insertRequest.get()
                .append(REQUEST_INSERT_HEAD)
                .append(APOSTROPHE)
                .append(encryptedVote.getId())
                .append(APOSTROPHE)
                .append(COMMA)
                .append(APOSTROPHE)
                .append(encryptedVote.getCenter())
                .append(APOSTROPHE)
                .append(COMMA)
                .append(APOSTROPHE)
                .append(encryptedVote.getChoice())
                .append(APOSTROPHE)
                .append(REQUEST_INSERT_FOOTER);

        return insertRequest.toString();
    }

    private String getDeleteRequest(Vote incorrectVote) {
        AtomicReference<StringBuilder> insertRequest = new AtomicReference<>(new StringBuilder());
        insertRequest.get()
                .append(REQUEST_DELETE)
                .append(APOSTROPHE)
                .append(incorrectVote.getId())
                .append(APOSTROPHE);

        return insertRequest.toString();
    }
}
