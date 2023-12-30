package com.wallet.Haja.repository;

import com.wallet.Haja.config.Connectiondb;
import com.wallet.Haja.entity.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository  implements CrudOperations<Currency, Long> {
    private PreparedStatement pstmt;
    private Connection connection;
    private ResultSet rs;
    @Override
    public Currency findById(Long id) {
        try {
            String SQL = "SELECT * FROM currency WHERE currency_id = ?";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Currency currency = new Currency();
                currency.setCurrencyId(rs.getLong("currency_id"));
                currency.setCurrencyName(rs.getString("currency_name"));
                currency.setCurrencyCode(rs.getString("currency_code"));
                return currency;
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
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        try {
            String SQL = "SELECT * FROM currency";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Currency currency = new Currency();
                currency.setCurrencyId(rs.getLong("currency_id"));
                currency.setCurrencyName(rs.getString("currency_name"));
                currency.setCurrencyCode(rs.getString("currency_code"));
                currencies.add(currency);
            }
            return currencies;
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
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave) {
        List<Currency> savedCurrencies = new ArrayList<>();
        try {
            connection = Connectiondb.getConnection();
            for (Currency currency : toSave) {
                String SQL = "INSERT INTO currency (currency_name, currency_code) VALUES (?, ?)";
                pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, currency.getCurrencyName());
                pstmt.setString(2, currency.getCurrencyCode());
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        currency.setCurrencyId(generatedKeys.getLong(1));
                        savedCurrencies.add(currency);
                    }
                }
            }
            return savedCurrencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public Currency save(Currency toSave) {
        try {
            connection = Connectiondb.getConnection();
            String SQL = "INSERT INTO currency (currency_name, currency_code) VALUES (?, ?)";
            pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, toSave.getCurrencyName());
            pstmt.setString(2, toSave.getCurrencyCode());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    toSave.setCurrencyId(generatedKeys.getLong(1));
                    return toSave;
                }
            }
            throw new RuntimeException("Failed to save currency: " + toSave.getCurrencyName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public Currency delete(Currency toDelete) {
        try {
            connection = Connectiondb.getConnection();
            String SQL = "DELETE FROM currency WHERE currency_id = ?";
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, toDelete.getCurrencyId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return toDelete;
            }
            throw new RuntimeException("Failed to delete currency with ID: " + toDelete.getCurrencyId());
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
