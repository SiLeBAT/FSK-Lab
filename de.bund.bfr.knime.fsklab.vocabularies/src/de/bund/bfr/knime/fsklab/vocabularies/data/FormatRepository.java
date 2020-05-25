package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Format;

public class FormatRepository implements BasicRepository<Format> {

    private final Connection connection;

    public FormatRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Format> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM format WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            return Optional.of(new Format(id, name, comment));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Format[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM format");

        ArrayList<Format> formatList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            formatList.add(new Format(id, name, comment));
        }

        return formatList.toArray(new Format[0]);
    }
}
