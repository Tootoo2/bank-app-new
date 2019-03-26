package app.db;

import java.sql.*;
import java.util.HashMap;

public class Database {
    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        connectToDb();
    }

    final String connectionURL = "jdbc:mysql://localhost/bank?user=root&password=root&serverTimezone=UTC";
    private Connection conn = null;
    private HashMap<String, PreparedStatement> preparedStatements = new HashMap<>();
    private HashMap<String, CallableStatement> preparedCallableStatements = new HashMap<>();



    /**
     * Returns a cached PreparedStatement if possible, else caches it for future use
     */
    public PreparedStatement prepareStatement(String SQLQuery) {
        PreparedStatement ps = preparedStatements.get(SQLQuery);
        if (ps == null) {
            try {
                ps = conn.prepareStatement(SQLQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ps;
    }
    public CallableStatement prepareCallableStatement(String SQLQuery){
        CallableStatement call = preparedCallableStatements.get(SQLQuery);
        if (call == null) {
            try { call = conn.prepareCall(SQLQuery); }
            catch (SQLException e) { e.printStackTrace(); }
        }
        return call;
    }

    private void connectToDb() {
        try {
            conn = DriverManager.getConnection(connectionURL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
