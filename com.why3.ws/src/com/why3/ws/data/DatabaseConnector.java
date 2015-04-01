package com.why3.ws.data;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DatabaseConnector {
	
	private static String host = "jdbc:mysql://why3db.clktbizogeda.eu-west-1.rds.amazonaws.com:3306/why3db";
    private static String uName = "why3admin";
    private static String uPass = "adminwhy3";	
	private Connection con;
	private java.sql.PreparedStatement attemptInsert;  
	private java.sql.PreparedStatement contextInsert;  
	private java.sql.PreparedStatement proverInsert;  
	private java.sql.PreparedStatement theoremInsert;  
	private java.sql.PreparedStatement userInsert;  
	private java.sql.PreparedStatement sessionInsert;  
	private java.sql.PreparedStatement scenarioInsert;  
	private java.sql.PreparedStatement scenarioQuery;  
	
	protected DatabaseConnector() {
		try {
			con = (Connection) DriverManager.getConnection( host, uName, uPass );
			proverInsert = con.prepareStatement("INSERT LOW_PRIORITY INTO provers (name, version) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
			contextInsert = con.prepareStatement("INSERT LOW_PRIORITY INTO contexts (id, context) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
			theoremInsert = con.prepareStatement("INSERT LOW_PRIORITY INTO theorems (types, defs, hyps, goal) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			attemptInsert = con.prepareStatement("INSERT LOW_PRIORITY INTO attempts (theorem, context, prover, user, status, timestamp, duration) values (?, ?, ?, ?, ?, NOW(), ?)");
			userInsert = con.prepareStatement("INSERT IGNORE INTO users (id, ip) values (?, ?)");
			sessionInsert = con.prepareStatement("INSERT LOW_PRIORITY INTO sessions (user, scenario, theorem, start) values (?, ?, ?, NOW())", Statement.RETURN_GENERATED_KEYS);
			scenarioInsert = con.prepareStatement("INSERT LOW_PRIORITY INTO scenarious (name, body) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
			scenarioQuery = con.prepareStatement("SELECT id FROM scenarious WHERE name = ?");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database connector initialisation failed: " + e.toString());
		}
	}

	protected void startTransaction() throws SQLException {
		con.setAutoCommit(false);
	}

	protected void commitTransaction() throws SQLException {
		con.commit();
	}	

	protected void rollbackTransaction() throws SQLException {
		con.rollback();
	}	
	
	protected long queryScenario(String name) throws SQLException {
		scenarioQuery.clearParameters();
		scenarioQuery.setString(1, name);
		if (!scenarioQuery.execute()) {
			return 1; // undefined scenario code
		} else {
			if (scenarioQuery.getResultSet().first()) {
				return scenarioQuery.getResultSet().getLong(1);
			} else {
				return 0;
			}
		}
	}

	/**
	 * The call may fail if there is a matching pair; note that id in table users must be not a key as duplicates should be permitted (same user from differing locations)
	 * @param user
	 * @param ip
	 * @throws SQLException
	 */
	protected void insertUser(long user, long ip) throws SQLException {
		userInsert.clearParameters();
		userInsert.setLong(1, user);
		userInsert.setLong(2, ip);
		userInsert.execute();
	}	
	
	protected long insertSession(long user, long scenario, long theorem) throws SQLException {
		sessionInsert.clearParameters();
		sessionInsert.setLong(1, user);
		sessionInsert.setLong(2, scenario);
		sessionInsert.setLong(3, theorem);
		sessionInsert.execute();
		ResultSet rs = sessionInsert.getGeneratedKeys();
		if (!rs.next()) {
			rs.close();
			throw new SQLException("Failed retrieving theorem id ");
		} else {
			long l = rs.getLong(1);
			rs.close();
			return l;
		}
	}

	protected long insertTheorem(String types, String defs, String hyps, String goal) throws SQLException {
		theoremInsert.clearParameters();
		theoremInsert.setString(1, types);
		theoremInsert.setString(2, defs);
		theoremInsert.setString(3, hyps);
		theoremInsert.setString(4, goal);
		theoremInsert.execute();
		ResultSet rs = theoremInsert.getGeneratedKeys();
		if (!rs.next()) {
			rs.close();
			throw new SQLException("Failed retrieving theorem id ");
		} else {
			long l = rs.getLong(1);
			rs.close();
			return l;
		}
	}
	
	protected void insertAttempt(long theorem, long context, int prover, long user, int status, long duration) throws SQLException {
		attemptInsert.clearParameters();		
		attemptInsert.setLong(1, theorem);
		attemptInsert.setLong(2, context);
		attemptInsert.setInt(3, prover);
		attemptInsert.setLong(4, user);
		attemptInsert.setInt(5, status);
		attemptInsert.setLong(6, duration);
		attemptInsert.execute();
	}
	
	/**
	 * Insert a prover description
	 * @param name prover name
	 * @param version prover version
	 * @return unique prover key id
	 * @throws SQLException
	 */
	protected long insertProver(String name, int version) throws SQLException {
		proverInsert.clearParameters();		
		proverInsert.setString(1, name);
		proverInsert.setInt(2, version);
		proverInsert.execute();
		ResultSet rs = proverInsert.getGeneratedKeys();
		if (!rs.next()) {
			rs.close();
			throw new SQLException("Failed retrieving prover id ");
		} else {
			long l = rs.getLong(1);
			rs.close();
			return l;
		}
	}
	
}
