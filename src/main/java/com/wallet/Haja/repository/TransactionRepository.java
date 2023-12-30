package com.wallet.Haja.repository;

import com.wallet.Haja.config.Connectiondb;
import com.wallet.Haja.entity.TransferHistory;
import com.wallet.Haja.entity.Account;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository implements CrudOperations<TransferHistory, Long> {
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private   AccountRepository accountRepository;

    @Override
    public TransferHistory findById(Long id) {
        try {
            String SQL = "SELECT * FROM transfer_history WHERE transfer_history_id = ?";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTransferHistory(rs);
            }
            throw new RuntimeException("TransferHistory not found with ID: " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public List<TransferHistory> findAll() {
        List<TransferHistory> transferHistories = new ArrayList<>();
        try {
            String SQL = "SELECT * FROM transfer_history";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                TransferHistory transferHistory = mapResultSetToTransferHistory(rs);
                transferHistories.add(transferHistory);
            }
            return transferHistories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public List<TransferHistory> saveAll(List<TransferHistory> toSave) {
        List<TransferHistory> savedTransferHistories = new ArrayList<>();
        try {
            connection = Connectiondb.getConnection();
            for (TransferHistory transferHistory : toSave) {
                String SQL = "INSERT INTO transfer_history (debit_transaction_id, credit_transaction_id, amount, transfer_date) VALUES (?, ?, ?, ?)";
                pstmt = connection.prepareStatement(SQL);
                pstmt.setLong(1, transferHistory.getDebitTransaction().getAccountId());
                pstmt.setLong(2, transferHistory.getCreditTransaction().getAccountId());
                pstmt.setDouble(3, transferHistory.getAmount());
                pstmt.setTimestamp(4, Timestamp.valueOf(transferHistory.getTransferDate()));

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    savedTransferHistories.add(transferHistory);
                }
            }
            return savedTransferHistories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public TransferHistory save(TransferHistory toSave) {
        try {
            connection = Connectiondb.getConnection();
            String SQL = "INSERT INTO transfer_history (debit_transaction_id, credit_transaction_id, amount, transfer_date) VALUES (?, ?, ?, ?)";
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, toSave.getDebitTransaction().getAccountId());
            pstmt.setLong(2, toSave.getCreditTransaction().getAccountId());
            pstmt.setDouble(3, toSave.getAmount());
            pstmt.setTimestamp(4, Timestamp.valueOf(toSave.getTransferDate()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return toSave;
            }
            throw new RuntimeException("Failed to save TransferHistory");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public TransferHistory delete(TransferHistory toDelete) {
        try {
            String SQL = "DELETE FROM transfer_history WHERE transfer_history_id = ?";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, toDelete.getTransferHistoryId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return toDelete;
            }
            throw new RuntimeException("Failed to delete TransferHistory with ID: " + toDelete.getTransferHistoryId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }


    private TransferHistory mapResultSetToTransferHistory(ResultSet rs) throws SQLException {
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setTransferHistoryId(rs.getLong("transfer_history_id"));
        transferHistory.setAmount(rs.getDouble("amount"));
        transferHistory.setTransferDate(rs.getTimestamp("transfer_date").toLocalDateTime());
        Long debitTransactionId = rs.getLong("debit_transaction_id");
        Long creditTransactionId = rs.getLong("credit_transaction_id");
        Account debitTransaction = accountRepository.findById(debitTransactionId);
        Account creditTransaction = accountRepository.findById(creditTransactionId);
        transferHistory.setDebitTransaction(debitTransaction);
        transferHistory.setCreditTransaction(creditTransaction);

        return transferHistory;
    }

    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
