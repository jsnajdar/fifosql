package cz.iens.fifosql.database;

import java.sql.Connection;

public interface JdbcDatabase {

	Connection getConnection();
	void close();
	
}
