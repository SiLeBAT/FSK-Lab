package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Region;

public class RegionRepository implements BasicRepository<Region> {

	private final Connection connection;

	public RegionRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<Region> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM region WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("nuts");

				return Optional.of(new Region(id, name, ssd));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public Region[] getAll() {

		ArrayList<Region> areaList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM region");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("nuts");

				areaList.add(new Region(id, name, ssd));
			}
		} catch (SQLException err) {
		}

		return areaList.toArray(new Region[0]);
	}
}
