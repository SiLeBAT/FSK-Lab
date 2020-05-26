package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Hazard;

public class HazardRepository implements BasicRepository<Hazard> {

    private final Connection connection;

    public HazardRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Hazard> getById(int id) {
    	
    	try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM hazard WHERE id = " + id);

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String ssd = resultSet.getString("ssd");
                return Optional.of(new Hazard(id, name, ssd));
            }
            
            return Optional.empty();
    	} catch (SQLException err) {
    		return Optional.empty();
    	}
    }

    @Override
    public Hazard[] getAll() {
    	
        ArrayList<Hazard> hazardList = new ArrayList<>();

    	try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM hazard");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String ssd = resultSet.getString("ssd");

                hazardList.add(new Hazard(id, name, ssd));
            }
    	} catch (SQLException err) {
    	}

        return hazardList.toArray(new Hazard[0]);
    }
}
