package com.wallet.Haja.repository;

import com.wallet.Haja.config.Connectiondb;
import com.wallet.Haja.entity.CurrencyValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyValueRepository implements CrudOperations<CurrencyValue, Long> {
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet rs;

    @Override
    public CurrencyValue findById(Long id) {
        try {
            String SQL = "SELECT * FROM currency_value WHERE currency_value_id = ?";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCurrencyValue(rs);
            }
            throw new RuntimeException("CurrencyValue not found with ID: " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public List<CurrencyValue> findAll() {
        List<CurrencyValue> currencyValues = new ArrayList<>();
        try {
            String SQL = "SELECT * FROM currency_value";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CurrencyValue currencyValue = mapResultSetToCurrencyValue(rs);
                currencyValues.add(currencyValue);
            }
            return currencyValues;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public List<CurrencyValue> saveAll(List<CurrencyValue> toSave) {
        List<CurrencyValue> savedCurrencyValues = new ArrayList<>();
        try {
            connection = Connectiondb.getConnection();
            for (CurrencyValue currencyValue : toSave) {
                String SQL = "INSERT INTO currency_value (source_currency_id, destination_currency_id, value, effective_date) VALUES (?, ?, ?, ?)";
                pstmt = connection.prepareStatement(SQL);
                pstmt.setInt(1, currencyValue.getSourceCurrencyId());
                pstmt.setInt(2, currencyValue.getDestinationCurrencyId());
                pstmt.setDouble(3, currencyValue.getValue());
                pstmt.setDate(4, java.sql.Date.valueOf(currencyValue.getEffectiveDate()));

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    savedCurrencyValues.add(currencyValue);
                }
            }
            return savedCurrencyValues;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public CurrencyValue save(CurrencyValue toSave) {
        try {
            connection = Connectiondb.getConnection();
            String SQL = "INSERT INTO currency_value (source_currency_id, destination_currency_id, value, effective_date) VALUES (?, ?, ?, ?)";
            pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, toSave.getSourceCurrencyId());
            pstmt.setInt(2, toSave.getDestinationCurrencyId());
            pstmt.setDouble(3, toSave.getValue());
            pstmt.setDate(4, java.sql.Date.valueOf(toSave.getEffectiveDate()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return toSave;
            }
            throw new RuntimeException("Failed to save CurrencyValue");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    @Override
    public CurrencyValue delete(CurrencyValue toDelete) {
        try {
            String SQL = "DELETE FROM currency_value WHERE currency_value_id = ?";
            connection = Connectiondb.getConnection();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1, toDelete.getCurrencyValueId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return toDelete;
            }
            throw new RuntimeException("Failed to delete CurrencyValue with ID: " + toDelete.getCurrencyValueId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }
    }

    private CurrencyValue mapResultSetToCurrencyValue(ResultSet rs) throws SQLException {
        CurrencyValue currencyValue = new CurrencyValue();
        currencyValue.setCurrencyValueId(rs.getLong("currency_value_id"));
        currencyValue.setSourceCurrencyId(rs.getInt("source_currency_id"));
        currencyValue.setDestinationCurrencyId(rs.getInt("destination_currency_id"));
        currencyValue.setValue(rs.getDouble("value"));
        currencyValue.setEffectiveDate(rs.getDate("effective_date").toLocalDate());
        return currencyValue;
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
