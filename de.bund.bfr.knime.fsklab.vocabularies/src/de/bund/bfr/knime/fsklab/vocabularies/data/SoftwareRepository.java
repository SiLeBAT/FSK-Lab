package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Software;

public class SoftwareRepository implements BasicRepository<Software> {

	private final Connection connection;

	public SoftwareRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<Software> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM software WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				return Optional.of(new Software(id, name));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public Software[] getAll() {
		ArrayList<Software> softwareList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM software");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				softwareList.add(new Software(id, name));
			}
		} catch (SQLException err) {
		}

		return softwareList.toArray(new Software[0]);
	}
}
