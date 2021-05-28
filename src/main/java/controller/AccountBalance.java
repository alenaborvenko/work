package controller;

import java.math.BigDecimal;

/**
 * Класс для парсинга запроса для запроса пополнения баланса счета
 */
public class AccountBalance {
    private String account;
    private String passport;
    private BigDecimal balance;

    public AccountBalance(String account, String passport, BigDecimal balance) {
        this.account = account;
        this.passport = passport;
        this.balance = balance;
    }

    public String getNumber() {
        return account;
    }

    public void setNumber(String number) {
        this.account = number;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
