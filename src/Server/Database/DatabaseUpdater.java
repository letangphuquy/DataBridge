package Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.User;
import Model.UserPair;
import Rules.Relationship;

public class DatabaseUpdater {
    private DatabaseUpdater() {}

    private static void updateTable(String tableName, String[] columns, Object[] values, String[] idColumns, Object[] idValues) throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        synchronized (connection) {
            String command = "UPDATE " + tableName;
            if (columns == null) return ;
            assert columns.length == values.length;
            command += " SET ";
            String[] setCommands = new String[columns.length];
            for (int i = 0; i < columns.length; i++)
                setCommands[i] = columns[i] + " = ?";
            command += String.join(",", setCommands);
            assert idColumns != null;
            assert idColumns.length == idValues.length;
            command += " WHERE ";
            String[] whereCommands = new String[idColumns.length];
            for (int i = 0; i < idColumns.length; i++)
                whereCommands[i] = idColumns[i] + " = ?";
            command += String.join(" AND ", whereCommands);

            PreparedStatement statement = connection.prepareStatement(command);
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Character)
                    values[i] += "";
                statement.setObject(i+1, values[i]);
                // System.out.println("Set type " + metadata.getColumnType(i+1) + " for column " + metadata.getColumnName(i+1) + " in table " + tableName);
            }
            for (int i = 0; i < idValues.length; i++) {
                if (idValues[i] instanceof Character)
                    idValues[i] += "";
                statement.setObject(values.length + 1 + i, idValues[i]);
            }

            System.out.println("Executing command: " + command);
            statement.executeUpdate();
        }
    }

	public static void updateUser(long userID, User user) {
        try {
            updateTable("Users", Data.getColumnsOf("Users"), user.toObjectArray(), new String[] {"user_id"}, new Object[] {userID});
        } catch (SQLException e) {
            System.out.println("Could not update user " + user.getUserID() + " in database");
            e.printStackTrace();
        }
        Data.users.put(userID, user);
        if (user.isPrivate()) 
            Data.publicUsers.remove(userID);
        else
            Data.publicUsers.add(userID);
	}

    public static UserPair updateRelationship(long userAID, long userBID, Relationship attitude) {
        boolean isSwapped = false;
        if (userAID > userBID) {
            userAID ^= userBID;
            userBID ^= userAID;
            userAID ^= userBID;
            isSwapped = true;
        }
        Relationship newAttitudeA = attitude;
        Relationship newAttitudeB = Relationship.NONE;
        if (isSwapped) {
            newAttitudeA = Relationship.NONE;
            newAttitudeB = attitude;
        }

        User userA = Data.users.get(userAID);
        User userB = Data.users.get(userBID);
        UserPair pair = new UserPair(userA, userB);
        pair.setAttitudes(newAttitudeA, newAttitudeB);

        if (!Data.relationships.containsKey(pair)) {
            DatabaseInserter.addRelationship(pair);
        } else {
            UserPair oldPair = Data.relationships.get(pair);
            if (!isSwapped)
                pair.setAttitudeB(oldPair.getAttitudeB());
            else
                pair.setAttitudeA(oldPair.getAttitudeA());
            Data.relationships.put(pair, pair);
        }

        try {
            updateTable("Friendship", Data.getColumnsOf("Friendship"), pair.toObjectArray(), new String[] {"user_A", "user_B"}, new Object[] {userAID, userBID});
        } catch (SQLException e) {
            System.out.println("Could not update relationship between " + userAID + " and " + userBID + " in database");
            e.printStackTrace();
        }
        return pair;
    }
}
