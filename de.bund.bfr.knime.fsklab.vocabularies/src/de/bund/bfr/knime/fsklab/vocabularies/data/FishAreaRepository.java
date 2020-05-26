package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.FishArea;

public class FishAreaRepository implements BasicRepository<FishArea> {

	private final Connection connection;

	public FishAreaRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<FishArea> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM fish_area WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("ssd");

				return Optional.of(new FishArea(id, name, ssd));
			}

			return Optional.empty();

		} catch (SQLException e) {
			return Optional.empty();
		}
	}

	@Override
	public FishArea[] getAll() {

		ArrayList<FishArea> areaList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM fish_area");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String ssd = resultSet.getString("ssd");

				areaList.add(new FishArea(id, name, ssd));
			}
		} catch (SQLException e) {
		}

		return areaList.toArray(new FishArea[0]);
	}
}
