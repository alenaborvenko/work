package model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Card {
    int id;
    private Account account;

    public Card(int id, Account account) {
        this.id = id;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && Objects.equals(account, card.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Card.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("account=" + account)
                .toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
