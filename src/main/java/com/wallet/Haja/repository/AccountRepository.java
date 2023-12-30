package com.wallet.Haja.repository;

import com.wallet.Haja.config.Connectiondb;
import com.wallet.Haja.entity.Account;
import com.wallet.Haja.entity.AccountType;
import com.wallet.Haja.entity.Currency;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor

public class AccountRepository implements CrudOperations<Account, Long>{
    private final CurrencyRepository currencyRepository;
    @Override
    public Account findById(Long id) {
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            String SQL = "SELECT * FROM account WHERE account_id = ?";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Account account = new Account();
                account.setAccountId(rs.getLong("account_id"));
                account.setAccountName(rs.getString("account_name"));
                long currencyId = rs.getLong("currency_id");
                if (currencyId != 0L) {
                    Currency currency = this.currencyRepository.findById(currencyId);
                    account.setCurrency(currency);
                }
                account.setAccountType(AccountType.valueOf(rs.getString("account_type")));
                return account;
            }
             throw new RuntimeException("Not found");
        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null || connection != null) {
                try {
                    pstmt.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            String SQL = "SELECT * FROM account";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Account account = new Account();
                account.setAccountId(rs.getLong("account_id"));
                account.setAccountName(rs.getString("account_name"));
                long currencyId = rs.getLong("currency_id");
                if (currencyId != 0L) {
                    Currency currency = this.currencyRepository.findById(currencyId);
                    account.setCurrency(currency);
                }
                account.setAccountType(AccountType.valueOf(rs.getString("account_type")));
                accounts.add(account);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null || connection != null) {
                try {
                    pstmt.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        }
        return accounts;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        List<Account> savedAccounts = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = Connectiondb.getConnection();
            for (Account account : toSave) {
                String SQL = "INSERT INTO account (account_name, currency_id, account_type) VALUES (?, ?, ?)";
                pstmt = connection.prepareStatement(SQL);
                pstmt.setString(1, account.getAccountName());
                pstmt.setLong(2, account.getCurrency().getCurrencyId());
                pstmt.setString(3, account.getAccountType().toString());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    savedAccounts.add(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null || connection != null) {
                try {
                    pstmt.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        }
        return savedAccounts;
    }

    @Override
    public Account save(Account toSave) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = Connectiondb.getConnection();
            String SQL = "INSERT INTO account (account_name, currency_id, account_type) VALUES (?, ?, ?)";
            pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, toSave.getAccountName());
            pstmt.setLong(2, toSave.getCurrency().getCurrencyId());

            pstmt.setString(3, toSave.getAccountType().toString());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return toSave;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null || connection != null) {
                try {
                    pstmt.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        }
        return null;
    }


    @Override
    public Account delete(Account toDelete) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = Connectiondb.getConnection();
            String SQL = "DELETE FROM account WHERE account_id = ?";
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, toDelete.getAccountId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return toDelete;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null || connection != null) {
                try {
                    pstmt.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        }
        return null;
    }
}
