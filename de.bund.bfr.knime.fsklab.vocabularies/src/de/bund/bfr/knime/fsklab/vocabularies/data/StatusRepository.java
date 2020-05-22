package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Status;

public class StatusRepository implements BasicRepository<Status> {

    private final Connection connection;

    public StatusRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Status getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM status WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            return new Status(id, name, comment);
        } else {
            return null;
        }
    }

    @Override
    public Status[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM status");

        ArrayList<Status> statuses = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            statuses.add(new Status(id, name, comment));
        }

        return statuses.toArray(new Status[0]);
    }
}
