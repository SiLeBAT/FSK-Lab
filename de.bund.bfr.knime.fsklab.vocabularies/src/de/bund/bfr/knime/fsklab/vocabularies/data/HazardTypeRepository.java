package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.HazardType;

public class HazardTypeRepository implements BasicRepository<HazardType> {

	private final Connection connection;

	public HazardTypeRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<HazardType> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM hazard_type WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				return Optional.of(new HazardType(id, name));
			}

			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public HazardType[] getAll() {

		ArrayList<HazardType> typeList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM hazard_type");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");

				typeList.add(new HazardType(id, name));
			}
		} catch (SQLException err) {
		}

		return typeList.toArray(new HazardType[0]);
	}
}
