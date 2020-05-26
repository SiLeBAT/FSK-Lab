package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingMethod;

public class SamplingMethodRepository implements BasicRepository<SamplingMethod> {

	private final Connection connection;

	public SamplingMethodRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<SamplingMethod> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_method WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String sampmd = resultSet.getString("sampmd");
				String comment = resultSet.getString("comment");

				return Optional.of(new SamplingMethod(id, name, sampmd, comment));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public SamplingMethod[] getAll() {

		ArrayList<SamplingMethod> methodList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_method");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String sampmd = resultSet.getString("sampmd");
				String comment = resultSet.getString("comment");

				methodList.add(new SamplingMethod(id, name, sampmd, comment));
			}
		} catch (SQLException err) {
		}

		return methodList.toArray(new SamplingMethod[0]);
	}
}
