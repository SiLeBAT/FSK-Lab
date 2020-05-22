package de.bund.bfr.knime.fsklab.vocabularies.data;

import java.sql.SQLException;

public interface BasicRepository<T> {

    T getById(int id) throws SQLException;

    T[] getAll() throws SQLException;
}
