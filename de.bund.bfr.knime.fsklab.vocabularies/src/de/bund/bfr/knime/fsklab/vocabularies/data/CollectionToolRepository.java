package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.CollectionTool;

public class CollectionToolRepository implements BasicRepository<CollectionTool> {

    private final Connection connection;

    public CollectionToolRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<CollectionTool> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM collection_tool WHERE id = '" + id + "'");

        if (resultSet.next()) {
            String name = resultSet.getString("name");

            return Optional.of(new CollectionTool(id, name));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public CollectionTool[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM collection_tool");

        ArrayList<CollectionTool> tools = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            tools.add(new CollectionTool(id, name));
        }

        return tools.toArray(new CollectionTool[0]);
    }
}
