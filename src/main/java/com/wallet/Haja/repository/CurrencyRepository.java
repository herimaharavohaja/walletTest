package com.wallet.Haja.repository;

import com.wallet.Haja.config.Connectiondb;
import com.wallet.Haja.entity.Account;
import com.wallet.Haja.entity.AccountType;
import com.wallet.Haja.entity.Currency;
import com.wallet.Haja.entity.CurrencyValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return null;
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave) {
        return null;
    }

    @Override
    public Currency save(Currency toSave) {
        return null;
    }

    @Override
    public Currency delete(Currency toDelete) {
        return null;
    }
}
