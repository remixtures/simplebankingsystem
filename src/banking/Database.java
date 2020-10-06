package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final String url;

    public Database(String url) {
        this.url = url;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;

    }

    public boolean isThereAClient(String card){
        String findID = "SELECT id FROM card WHERE number = '" + card + "';";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(findID)){

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean isThereACardNumber(String card, String PIN){
        String findCard = "SELECT id FROM card WHERE number = '" + card + "' AND pin = '" + PIN + "';";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(findCard)){

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    protected void createTable() {
        String addAccount = "CREATE TABLE IF NOT EXISTS card ("
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	number TEXT,"
                + "	pin TEXT,"
                + " balance INTEGER DEFAULT 0"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(addAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void decreaseBalance(int amount, String cardNumber) {
        String updateAccount = "UPDATE card SET balance = balance - " + amount + " WHERE number = '" + cardNumber + "'";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(updateAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void increaseBalance(int amount, String cardNumber) {
        String updateAccount = "UPDATE card SET balance = balance + " + amount + " WHERE number = '" + cardNumber + "'";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(updateAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    protected int getBalance(String cardNumber) {
        String checkMoney = "SELECT balance FROM card WHERE number = '" + cardNumber + "';";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(checkMoney)){
            while (rs.next()) {
                return rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    protected void insert(String cardNumber, String pinNumber, int balance) {
        String insertIntoAccount = "INSERT INTO card(number,pin,balance) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertIntoAccount)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pinNumber);
            pstmt.setInt(3, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void removeAccount(String cardNumber) {
        String deletingAccount = "DELETE FROM card WHERE number = '" + cardNumber + "';";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(deletingAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}