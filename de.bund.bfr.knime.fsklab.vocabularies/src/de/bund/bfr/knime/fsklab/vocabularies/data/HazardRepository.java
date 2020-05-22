package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.Hazard;

public class HazardRepository implements BasicRepository<Hazard> {

    private final Connection connection;

    public HazardRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Hazard getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hazard WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");

            return new Hazard(id, name, ssd);
        } else {
            return null;
        }
    }

    @Override
    public Hazard[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hazard");

        ArrayList<Hazard> hazardList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");

            hazardList.add(new Hazard(id, name, ssd));
        }

        return hazardList.toArray(new Hazard[0]);
    }
}
