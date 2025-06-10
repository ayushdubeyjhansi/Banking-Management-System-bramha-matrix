package dao;

import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private Connection conn;

    public TransactionDAO(Connection conn) {
        this.conn = conn;
    }

    // Add a new transaction
    public void addTransaction(int userId, String type, double amount) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, type, amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        }
    }

    // Get all transactions for a specific user, sorted by most recent first
    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT txn_id, user_id, type, amount, txn_date FROM transactions WHERE user_id = ? ORDER BY txn_date DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction txn = new Transaction();
                    txn.setTxnId(rs.getInt("txn_id"));
                    txn.setUserId(rs.getInt("user_id"));
                    txn.setType(rs.getString("type"));
                    txn.setAmount(rs.getDouble("amount"));
                    txn.setTxnDate(rs.getTimestamp("txn_date"));
                    transactions.add(txn);
                }
            }
        }
        return transactions;
    }

    // Optional: get last N transactions
    public List<Transaction> getRecentTransactions(int userId, int limit) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT txn_id, user_id, type, amount, txn_date FROM transactions WHERE user_id = ? ORDER BY txn_date DESC LIMIT ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction txn = new Transaction();
                    txn.setTxnId(rs.getInt("txn_id"));
                    txn.setUserId(rs.getInt("user_id"));
                    txn.setType(rs.getString("type"));
                    txn.setAmount(rs.getDouble("amount"));
                    txn.setTxnDate(rs.getTimestamp("txn_date"));
                    transactions.add(txn);
                }
            }
        }
        return transactions;
    }
    // Get all transactions for all users (for Admin view)
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY txn_date DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Transaction txn = new Transaction();
                txn.setTxnId(rs.getInt("txn_id"));
                txn.setUserId(rs.getInt("user_id"));
                txn.setType(rs.getString("type"));
                txn.setAmount(rs.getDouble("amount"));
                txn.setTxnDate(rs.getTimestamp("txn_date"));
                transactions.add(txn);
            }
        }
        return transactions;
    }

}
