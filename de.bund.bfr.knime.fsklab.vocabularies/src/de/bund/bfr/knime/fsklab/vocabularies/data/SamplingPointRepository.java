package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.SamplingPoint;

public class SamplingPointRepository implements BasicRepository<SamplingPoint> {

	private final Connection connection;

	public SamplingPointRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<SamplingPoint> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_point WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String sampnt = resultSet.getString("sampnt");
				return Optional.of(new SamplingPoint(id, name, sampnt));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
	public SamplingPoint[] getAll() {

		ArrayList<SamplingPoint> pointList = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sampling_point");

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String sampnt = resultSet.getString("sampnt");

				pointList.add(new SamplingPoint(id, name, sampnt));
			}
		} catch (SQLException err) {
		}

		return pointList.toArray(new SamplingPoint[0]);
	}
}
