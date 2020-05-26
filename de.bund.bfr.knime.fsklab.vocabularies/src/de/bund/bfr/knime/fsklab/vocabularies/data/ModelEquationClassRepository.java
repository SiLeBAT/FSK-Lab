package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ModelEquationClass;

public class ModelEquationClassRepository implements BasicRepository<ModelEquationClass> {

	private final Connection connection;

	public ModelEquationClassRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<ModelEquationClass> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM model_equation_class WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				return Optional.of(new ModelEquationClass(id, name));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public ModelEquationClass[] getAll() {

		ArrayList<ModelEquationClass> classList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM model_equation_class");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");

				classList.add(new ModelEquationClass(id, name));
			}
		} catch (SQLException err) {
		}

		return classList.toArray(new ModelEquationClass[0]);
	}
}
