package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.PublicationStatus;

public class PublicationStatusRepository implements BasicRepository<PublicationStatus> {

    private final Connection connection;

    public PublicationStatusRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PublicationStatus getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM publication_status WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            return new PublicationStatus(id, name, comment);
        } else {
            return null;
        }
    }

    @Override
    public PublicationStatus[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM publication_status");

        ArrayList<PublicationStatus> statusList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            statusList.add(new PublicationStatus(id, name, comment));
        }

        return statusList.toArray(new PublicationStatus[0]);
    }
}
