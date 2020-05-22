package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Source;

public class SourceRepository implements BasicRepository<Source> {

    private final Connection connection;

    public SourceRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Source getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sources WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            return new Source(id, name, comment);
        } else {
            return null;
        }
    }

    @Override
    public Source[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sources");

        ArrayList<Source> sourceList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            sourceList.add(new Source(id, name, comment));
        }

        return sourceList.toArray(new Source[0]);
    }
}
