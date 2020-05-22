package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingMethod;

public class SamplingMethodRepository implements BasicRepository<SamplingMethod> {

    private final Connection connection;

    public SamplingMethodRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SamplingMethod getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_method WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String sampmd = resultSet.getString("sampmd");
            String comment = resultSet.getString("comment");

            return new SamplingMethod(id, name, sampmd, comment);
        } else {
            return null;
        }
    }

    @Override
    public SamplingMethod[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_method");

        ArrayList<SamplingMethod> methodList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String sampmd = resultSet.getString("sampmd");
            String comment = resultSet.getString("comment");

            methodList.add(new SamplingMethod(id, name, sampmd, comment));
        }

        return methodList.toArray(new SamplingMethod[0]);
    }
}
