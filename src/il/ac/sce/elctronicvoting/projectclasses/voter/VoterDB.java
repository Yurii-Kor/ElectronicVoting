package il.ac.sce.elctronicvoting.projectclasses.voter;

import il.ac.sce.elctronicvoting.projectclasses.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class VoterDB {
    private static final String TABLE_NAME = "voters";
    private static final String FIELD_ID = "id";
    private static final String FIELD_CENTER = "center";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_PASSWORD = "password";
    private static final String COMMA = ", ";
    private static final String APOSTROPHE = "'";
    private static final String REQUEST_INSERT_HEAD = "INSERT INTO " + TABLE_NAME + " (id, center, name, password) VALUES (";
    private static final String REQUEST_INSERT_FOOTER = ")";
    private static final String REQUEST_SELECT_ALL_VOTERS_ID = "SELECT id FROM " + TABLE_NAME;
    private static final String REQUEST_SELECT_ONE_VOTER = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String REQUEST_COUNT = "SELECT COUNT(*) FROM " + TABLE_NAME;

    String[] getEncryptedIdArray() {
        List<String> hashedIdList = new ArrayList<>();

        try (Connection connection = DBConnection.getDBConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(REQUEST_SELECT_ALL_VOTERS_ID)) {

            while (resultSet.next()) {
                hashedIdList.add(resultSet.getString(FIELD_ID));
            }
        } catch (SQLException | ClassNotFoundException ignored) {
            return new String[0];
        }

        return hashedIdList.toArray(new String[0]);
    }

    Voter getVoterFromDB(String enteredEncryptedId) {
        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement stmt = connection.prepareStatement(REQUEST_SELECT_ONE_VOTER)) {

            stmt.setString(1, enteredEncryptedId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedId = rs.getString(FIELD_ID);
                String storedCenter = rs.getString(FIELD_CENTER);
                String storedName = rs.getString(FIELD_NAME);
                String storedPassword = rs.getString(FIELD_PASSWORD);

                return new Voter(storedId, storedCenter, storedName, storedPassword);
            }
        } catch (SQLException | ClassNotFoundException ignored) {}

        return null;
    }

    boolean isVotersEmpty() {
        try (Connection connection = DBConnection.getDBConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(REQUEST_COUNT)) {

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }

            return true;

        } catch (SQLException | ClassNotFoundException ignored) {}

        return false;
    }

    void addVotersToDB(Voter[] voters) {
        try (Connection connection = DBConnection.getDBConnection();
             Statement statement = connection.createStatement()) {

            for (Voter voter : voters) {
                String request = getInsertRequest(voter);
                statement.addBatch(request);
            }

            statement.executeBatch();
        } catch (SQLException | ClassNotFoundException ignored) {}
    }

    private String getInsertRequest(Voter encryptedVoter) {
        AtomicReference<StringBuilder> insertRequest = new AtomicReference<>(new StringBuilder());
        insertRequest.get()
                .append(REQUEST_INSERT_HEAD)
                .append(APOSTROPHE)
                .append(encryptedVoter.getId())
                .append(APOSTROPHE)
                .append(COMMA)
                .append(APOSTROPHE)
                .append(encryptedVoter.getCenter())
                .append(APOSTROPHE)
                .append(COMMA)
                .append(APOSTROPHE)
                .append(encryptedVoter.getName())
                .append(APOSTROPHE)
                .append(COMMA)
                .append(APOSTROPHE)
                .append(encryptedVoter.getPassword())
                .append(APOSTROPHE)
                .append(REQUEST_INSERT_FOOTER);

        return insertRequest.toString();
    }
}
