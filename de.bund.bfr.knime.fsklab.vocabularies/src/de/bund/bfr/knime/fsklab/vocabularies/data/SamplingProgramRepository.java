package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingProgram;

public class SamplingProgramRepository implements BasicRepository<SamplingProgram> {

	private final Connection connection;

	public SamplingProgramRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<SamplingProgram> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_program WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String progType = resultSet.getString("progType");
				return Optional.of(new SamplingProgram(id, name, progType));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public SamplingProgram[] getAll() {

		ArrayList<SamplingProgram> programs = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_program");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String progType = resultSet.getString("progType");

				programs.add(new SamplingProgram(id, name, progType));
			}
		} catch (SQLException err) {
		}

		return programs.toArray(new SamplingProgram[0]);
	}
}
