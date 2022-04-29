package cz.iens.fifosql.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.iens.fifosql.builder.UserBuilder;
import cz.iens.fifosql.database.JdbcDatabase;
import cz.iens.fifosql.exception.RepositoryException;
import cz.iens.fifosql.model.User;

public class UserJdbcRepository extends JdbcRepository {

	public UserJdbcRepository(JdbcDatabase database) {
		super(database);
	}

	public void drop() {
		executeStatement("DROP TABLE IF EXISTS SUSERS");
	}

	public void create() {
		executeStatement("CREATE TABLE IF NOT EXISTS SUSERS (USER_GUID VARCHAR2(36) PRIMARY KEY NOT NULL, USER_ID BIGINT NOT NULL, USER_NAME VARCHAR2(255) NOT NULL)");
	}

	public void insert(User user) {
		PreparedStatement st = getPreparedStatement("INSERT INTO SUSERS (USER_GUID, USER_ID, USER_NAME) VALUES (?, ?, ?)");
		try {
			st.setString(1, user.getGuid());
			st.setLong(2, user.getId());
			st.setString(3, user.getName());
		} catch (SQLException e) {
			throw new RepositoryException("Cannot set PreparedStatement parameters!", e);
		} 
		executePreparedStatement(st);
	}

	public List<User> selectAll() {
		PreparedStatement st = getPreparedStatement("SELECT USER_GUID, USER_ID, USER_NAME FROM SUSERS");
		ResultSet rs = getResultSet(st);
		List<User> users = new ArrayList<>();
		try {
			while(rs.next()) {
				users.add(new UserBuilder()
					.guid(rs.getString(1))
					.id(rs.getLong(2))
					.name(rs.getString(3))
					.build());
			}
		} catch (SQLException e) {
			throw new RepositoryException("Cannot fetch User from ResultSet!", e);
		} finally {
			closeResultSet(rs);
		}
		return users;
	}
	
	public void deleteAll() {
		executePreparedStatement(getPreparedStatement("DELETE FROM SUSERS"));
	}
	
}
