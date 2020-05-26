package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Availability;

public class AvailabilityRepository implements BasicRepository<Availability> {

    private final Connection connection;

    public AvailabilityRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Availability> getById(int id) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM availability WHERE id = '" + id + "'");
        	
			if (resultSet.next()) {
			    String name = resultSet.getString("name");
			    String comment = resultSet.getString("comment");

			    return Optional.of(new Availability(id, name, comment));
			}
			
			return Optional.empty();
		} catch (SQLException e) {
			return Optional.empty();
		}
    }

    @Override
    public Availability[] getAll() {
    	
        ArrayList<Availability> availabilityList = new ArrayList<>();
    	
		try {
			Statement statement = connection.createStatement();
			
	        ResultSet resultSet = statement.executeQuery("SELECT * FROM availability");

	        while (resultSet.next()) {
	            int id = resultSet.getInt("id");
	            String name = resultSet.getString("name");
	            String comment = resultSet.getString("comment");

	            availabilityList.add(new Availability(id, name, comment));
	        }
		} catch (SQLException e) {
		}

        return availabilityList.toArray(new Availability[0]);
    }
}
