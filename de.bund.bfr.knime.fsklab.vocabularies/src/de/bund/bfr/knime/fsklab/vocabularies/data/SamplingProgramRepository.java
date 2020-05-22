package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingProgram;

public class SamplingProgramRepository implements BasicRepository<SamplingProgram> {

    private final Connection connection;

    public SamplingProgramRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SamplingProgram getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_program WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String progType = resultSet.getString("progType");
            return new SamplingProgram(id, name, progType);
        } else {
            return null;
        }
    }

    @Override
    public SamplingProgram[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_program");

        ArrayList<SamplingProgram> programs = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String progType = resultSet.getString("progType");

            programs.add(new SamplingProgram(id, name, progType));
        }

        return programs.toArray(new SamplingProgram[0]);
    }
}
