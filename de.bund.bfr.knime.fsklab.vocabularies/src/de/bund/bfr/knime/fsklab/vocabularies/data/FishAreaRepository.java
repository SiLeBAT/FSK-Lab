package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.bund.bfr.knime.fsklab.vocabularies.domain.FishArea;

public class FishAreaRepository implements BasicRepository<FishArea> {

    private final Connection connection;

    public FishAreaRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public FishArea getById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM fish_area WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");

            return new FishArea(id, name, ssd);
        } else {
            return null;
        }
    }

    @Override
    public FishArea[] getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM fish_area");

        ArrayList<FishArea> areaList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String ssd = resultSet.getString("ssd");

            areaList.add(new FishArea(id, name, ssd));
        }

        return areaList.toArray(new FishArea[0]);
    }
}
