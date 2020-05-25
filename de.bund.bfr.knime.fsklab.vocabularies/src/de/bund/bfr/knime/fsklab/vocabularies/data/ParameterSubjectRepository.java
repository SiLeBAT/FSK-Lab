package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ParameterSubject;

public class ParameterSubjectRepository implements BasicRepository<ParameterSubject> {

    private final Connection connection;

    public ParameterSubjectRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<ParameterSubject> getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM parameter_subject WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            return Optional.of(new ParameterSubject(id, name));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ParameterSubject[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM parameter_subject");

        ArrayList<ParameterSubject> subjectList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            subjectList.add(new ParameterSubject(id, name));
        }

        return subjectList.toArray(new ParameterSubject[0]);
    }
}
