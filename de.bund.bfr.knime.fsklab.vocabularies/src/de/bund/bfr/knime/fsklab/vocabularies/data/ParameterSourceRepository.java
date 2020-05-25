package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ParameterSource;

public class ParameterSourceRepository implements BasicRepository<ParameterSource> {

    private final Connection connection;

    public ParameterSourceRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<ParameterSource> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM parameter_source WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            return Optional.of(new ParameterSource(id, name));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ParameterSource[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM parameter_source");

        ArrayList<ParameterSource> subjectList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            subjectList.add(new ParameterSource(id, name));
        }

        return subjectList.toArray(new ParameterSource[0]);
    }
}
