package app.db;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Entities.User;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * A Helper class for interacting with the Database using short-commands
 */
public abstract class DB {

    public static PreparedStatement prep(String SQLQuery) {
        return Database.getInstance().prepareStatement(SQLQuery);
    }

    public static CallableStatement call(String SQLQuery) {
        return Database.getInstance().prepareCallableStatement(SQLQuery);
    }

    public static User getMatchingUser(String socialNumber, String password) {
        User result = null;
        PreparedStatement ps = prep("SELECT * FROM user WHERE social_number = ? AND password = ?");
        try {
            ps.setString(1, socialNumber);
            ps.setString(2, password);
            result = (User) new ObjectMapper<>(User.class).mapOne(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Account> getAccounts(String socialNumber) {
        List<Account> result = null;
        PreparedStatement ps = prep("SELECT * FROM accounts WHERE account_owner = ?");
        try {
            ps.setString(1, socialNumber);
            result = (List<Account>) (List<?>) new ObjectMapper<>(Account.class).map(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Transaction> getTransactions(long accountId) {
        List<Transaction> result = null;
        PreparedStatement ps = prep("SELECT * FROM transaction WHERE sender = ? OR receiver = ?");
        try {
            ps.setLong(1, accountId);
            ps.setLong(2, accountId);
            result = (List<Transaction>) (List<?>) new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void changeAccountName(String accountName,String newAccountName, String securityNumber){
        String SQLQuery = "{ CALL change_account_name(?,?,?) }";
        CallableStatement cs= call(SQLQuery);
        try{
            cs.setString(1,accountName);
            cs.setString(2,newAccountName);
            cs.setString(3,securityNumber);
            cs.execute();
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void makeCardPayment(long sender) {
        String SQLQuery = "{ CALL make_transaction(?,?,?,?) }";
        String message = "Card Payment";
        long receiver = 44556781;
        float amount = 32f;
        CallableStatement cs = call(SQLQuery);
        try {
            cs.setString(1, message);
            cs.setLong(2, sender);
            cs.setLong(3, receiver);
            cs.setFloat(4, amount);
            cs.execute();
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteSavingsAccount(String accountName, String currentUser){
        String SQLQuery = "{ CALL delete_account(?,?,?) }";
        String accountType="Savings Account";
        CallableStatement cs = call(SQLQuery);
        try{
            cs.setString(1,accountName);
            cs.setString(2,accountType);
            cs.setString(3, currentUser);
            cs.execute();
            cs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void transferMoney(long sender, long receiver, float amount, String message) {
        String SQLQuery = "{ CALL make_transaction(?,?,?,?) }";
        CallableStatement cs = call(SQLQuery);
        try {
            cs.setString(1, message);
            cs.setLong(2, sender);
            cs.setLong(3, receiver);
            cs.setFloat(4, amount);
            cs.execute();
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createAccount(String name, String accountOwner) {
        String SQLQuery = "{ CALL create_new_account(?,?,?,?) }";
        String accountType = "Savings Account";
        float balance = 0;
        CallableStatement cs = call(SQLQuery);
        try {
            cs.setString(1, name);
            cs.setFloat(2, balance);
            cs.setString(3, accountType);
            cs.setString(4, accountOwner);
            cs.execute();
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Account getAccount(long accountNumber) {
        Account result = null;
        PreparedStatement ps = prep("SELECT * FROM accounts WHERE account_number = ?");
        try {
            ps.setLong(1, accountNumber);
            result = (Account) new ObjectMapper<>(Account.class).mapOne(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}