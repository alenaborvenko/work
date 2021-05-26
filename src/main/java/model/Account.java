package model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Account {
    private int id;
    private String number;
    private User user;
    private BigDecimal balance;

    public Account(int id, String  number, User user, BigDecimal balance) {
        this.id = id;
        this.number = number;
        this.user = user;
        this.balance = balance;
    }

    public Account(String number) {
        this.number = number;
    }

    public Account(String number, BigDecimal balance) {
        this.number = number;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(number, account.number) && Objects.equals(user, account.user) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, user, balance);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("number=" + number)
                .add("user=" + user)
                .toString();
    }
}
