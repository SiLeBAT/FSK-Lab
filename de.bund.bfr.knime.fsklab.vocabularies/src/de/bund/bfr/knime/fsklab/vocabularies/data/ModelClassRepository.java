package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.ModelClass;

public class ModelClassRepository implements BasicRepository<ModelClass> {

	private final Connection connection;

	public ModelClassRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Optional<ModelClass> getById(int id) {

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM model_class WHERE id = " + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				return Optional.of(new ModelClass(id, name));
			}
			return Optional.empty();
		} catch (SQLException err) {
			return Optional.empty();
		}
	}

	@Override
    public ModelClass[] getAll() {
    	
		ArrayList<ModelClass> classes = new ArrayList<>();

		try {
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery("SELECT * FROM model_class");
	        
	        while (resultSet.next()) {
	            int id = resultSet.getInt("id");
	            String name = resultSet.getString("name");

	            classes.add(new ModelClass(id, name));
	        }
    	} catch (SQLException err) {
    	}

        return classes.toArray(new ModelClass[0]);
    }
}
