package cz.iens.fifosql.repository;

import java.sql.*;

import cz.iens.fifosql.database.JdbcDatabase;
import cz.iens.fifosql.exception.RepositoryException;

public abstract class JdbcRepository implements Repository {

	private JdbcDatabase database;
	
	protected JdbcRepository(JdbcDatabase database) {
		super();
		this.database = database;
	}

	protected void executeStatement(String sql) {
		Connection conn = database.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RepositoryException("Cannot execute Statement!", e);
		}
		closeStatementAndConnection(stmt);
	}
	
	protected PreparedStatement getPreparedStatement(String sql) {
		Connection conn = database.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			throw new RepositoryException("Cannot get PreparedStatement!", e);
		}
		return stmt;
	}
	
	protected void executePreparedStatement(PreparedStatement stmt) {
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RepositoryException("Cannot execute PreparedStatement!", e);
		}
		closeStatementAndConnection(stmt);
	}
	
	protected ResultSet getResultSet(PreparedStatement stmt) {
		try {
			return stmt.executeQuery();
		} catch (SQLException e) {
			throw new RepositoryException("Cannot execute to ResultSet from PreparedStatement!", e);
		}
	}

	protected void closeResultSet(ResultSet rs) {
		Statement stmt = null;
		try {
			stmt = rs.getStatement();
		} catch (NullPointerException | SQLException e) {
			throw new RepositoryException("Cannot get statement from result set!", e);
		}
		try {
			rs.close();
		} catch (SQLException e) {
			throw new RepositoryException("Cannot execute prepared statement!", e);
		} 
		closeStatementAndConnection(stmt);
	}
	
	private void closeStatementAndConnection(Statement stmt) {
		Connection conn;
		try {
			conn = stmt.getConnection();
		} catch (NullPointerException | SQLException e) {
			throw new RepositoryException("Cannot get connection from prepared statement!", e);
		}
		try {
			stmt.close(); 
			stmt.close(); 			
		} catch (SQLException e) {
			throw new RepositoryException("Cannot execute prepared statement!", e);
		} finally {
			try { 
            	stmt.close();  
	        } catch(SQLException se) {
				System.err.println("Cannot close prepared statement!");
	        	se.printStackTrace();
	        }
			try { 
				 if(conn != null) {
					 conn.close(); 
				 }
			} catch(SQLException ce) { 
				System.err.println("Cannot close connection!");
				ce.printStackTrace();
		    } 
		}
	}

	
}
