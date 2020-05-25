package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ParameterDistribution;

public class ParameterDistributionRepository implements BasicRepository<ParameterDistribution> {

    private final Connection connection;

    public ParameterDistributionRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<ParameterDistribution> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM parameter_distribution WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            return Optional.of(new ParameterDistribution(id, name, comment));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ParameterDistribution[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM parameter_distribution");

        ArrayList<ParameterDistribution> distributions = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String comment = resultSet.getString("comment");

            distributions.add(new ParameterDistribution(id, name, comment));
        }

        return distributions.toArray(new ParameterDistribution[0]);
    }
}
