package de.bund.bfr.knime.fsklab.vocabularies.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestUtils {
	
	// Private constructor to not be used.
	private TestUtils() {
	}
	
	static Connection mockClosedConnection() throws SQLException {
		// Get a connection and close it.
		DriverManager.registerDriver(new org.h2.Driver());
		Connection closedConnection = DriverManager.getConnection("jdbc:h2:mem:");
		closedConnection.close();
	
		return closedConnection;
	}

}
