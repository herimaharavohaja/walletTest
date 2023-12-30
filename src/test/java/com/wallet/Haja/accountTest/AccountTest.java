package com.wallet.Haja.accountTest;

import com.wallet.Haja.entity.Account;
import com.wallet.Haja.entity.Currency;
import com.wallet.Haja.repository.AccountRepository;
import com.wallet.Haja.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
   private   CurrencyRepository currencyRepository;
    private AccountRepository accountRepository;
    @BeforeEach
    void setUp(){
        currencyRepository = new CurrencyRepository();
        accountRepository = new AccountRepository(currencyRepository);

    }

    @Test
    void find_by_id_currency_test_ok(){
        Currency currency = currencyRepository.findById(1L);
        assertEquals("EUR", currency.getCurrencyCode(), "ok");
    }

    @Test
    void find_by_id_account_test_ok(){
        Account account = accountRepository.findById(1L);
        assertEquals(
                "EUR",
                account.getCurrency().getCurrencyCode(),
                "ok"
        );
    }
}
