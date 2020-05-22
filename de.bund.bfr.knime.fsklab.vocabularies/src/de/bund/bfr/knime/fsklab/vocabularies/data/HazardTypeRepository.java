package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.HazardType;

public class HazardTypeRepository implements BasicRepository<HazardType> {

    private final Connection connection;

    public HazardTypeRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public HazardType getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hazard_type WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");

            return new HazardType(id, name);
        } else {
            return null;
        }
    }

    @Override
    public HazardType[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hazard_type");

        ArrayList<HazardType> typeList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            typeList.add(new HazardType(id, name));
        }

        return typeList.toArray(new HazardType[0]);
    }
}
