package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingPoint;

public class SamplingPointRepository implements BasicRepository<SamplingPoint> {

    private final Connection connection;

    public SamplingPointRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<SamplingPoint> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_point WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String sampnt = resultSet.getString("sampnt");
            return Optional.of(new SamplingPoint(id, name, sampnt));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public SamplingPoint[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_point");

        ArrayList<SamplingPoint> pointList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String sampnt = resultSet.getString("sampnt");

            pointList.add(new SamplingPoint(id, name, sampnt));
        }

        return pointList.toArray(new SamplingPoint[0]);
    }
}
