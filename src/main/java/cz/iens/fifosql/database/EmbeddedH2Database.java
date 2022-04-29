package cz.iens.fifosql.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

import cz.iens.fifosql.exception.DatabaseException;

public enum EmbeddedH2Database implements JdbcDatabase {

	INSTANCE; 
	
	private static final String JDBC_DRIVER = "org.h2.Driver";   
	private static final String DB_URL = "jdbc:h2:~/mem";  
	private static final String USER = "sa"; 
	private static final String PASS = ""; 
	   
	private JdbcConnectionPool connectionPool;
	
	private EmbeddedH2Database() {
		try {
			Class.forName(JDBC_DRIVER); 
			connectionPool = JdbcConnectionPool.create(DB_URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("Cannot find H2 driver!", e);
		}
	}
	
	public Connection getConnection() {
		try {
			return INSTANCE.getConnectionPool().getConnection();
		} catch (SQLException e) {
			throw new DatabaseException("Cannot connect to H2 database!", e);
		}
	}

	private JdbcConnectionPool getConnectionPool() {
		return connectionPool;
	}

	public void close() {
		INSTANCE.getConnectionPool().dispose();
	}
}
