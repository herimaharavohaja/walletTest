package com.wallet.Haja.repository;

import com.wallet.Haja.config.Connectiondb;
import com.wallet.Haja.entity.Account;
import com.wallet.Haja.entity.Balance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BalanceRepository implements CrudOperations<Balance, Long>{
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private AccountRepository accountRepository;

    @Override
    public Balance findById(Long id) {
        try {
            String SQL = "SELECT * FROM balance WHERE balance_id = ?";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Balance balance = new Balance();
                balance.setBalanceId(rs.getLong("balance_id"));
                balance.setBalanceDateTime(rs.getTimestamp("balance_datetime").toLocalDateTime());
                balance.setAmount(rs.getDouble("amount"));
                long currencyId = rs.getLong("currency_id");
                if (currencyId != 0L) {
                    Account account = this.accountRepository.findById(currencyId);
                    balance.setAccount(account);
                }
                return balance;
            }
            throw new RuntimeException("Balance not found with ID: " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public List<Balance> findAll() {
        List<Balance> balances = new ArrayList<>();
        try {
            String SQL = "SELECT * FROM balance";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Balance balance = new Balance();
                balance.setBalanceId(rs.getLong("balance_id"));
                balance.setBalanceDateTime(rs.getTimestamp("balance_datetime").toLocalDateTime());
                balance.setAmount(rs.getDouble("amount"));
                long accountId = rs.getLong("account_id");
                if (accountId != 0L) {
                    Account account = this.accountRepository.findById(accountId);
                    balance.setAccount(account);
                }
                balances.add(balance);
            }
            return balances;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }




    @Override
    public List<Balance> saveAll(List<Balance> toSave) {
        List<Balance> savedBalances = new ArrayList<>();
        try {
            connection = Connectiondb.getConnection();
            for (Balance balance : toSave) {
                String SQL = "INSERT INTO balance (balance_datetime, amount, account_id) VALUES (?, ?, ?)";
                pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setTimestamp(1, Timestamp.valueOf(balance.getBalanceDateTime()));
                pstmt.setDouble(2, balance.getAmount());
                pstmt.setLong(3, balance.getAccount().getAccountId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        balance.setBalanceId(generatedKeys.getLong(1));
                        savedBalances.add(balance);
                    }
                }
            }
            return savedBalances;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public Balance save(Balance toSave) {
        try {
            connection = Connectiondb.getConnection();
            String SQL = "INSERT INTO balance (balance_datetime, amount, account_id) VALUES (?, ?, ?)";
            pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, Timestamp.valueOf(toSave.getBalanceDateTime()));
            pstmt.setDouble(2, toSave.getAmount());
            pstmt.setLong(3, toSave.getAccount().getAccountId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    toSave.setBalanceId(generatedKeys.getLong(1));
                    return toSave;
                }
            }
            throw new RuntimeException("Failed to save balance");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }


    @Override
    public Balance delete(Balance toDelete) {
        try {
            String SQL = "DELETE FROM balance WHERE balance_id = ?";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, toDelete.getBalanceId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return toDelete;
            }
            throw new RuntimeException("Failed to delete balance with ID: " + toDelete.getBalanceId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
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
