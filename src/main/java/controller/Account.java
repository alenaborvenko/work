package controller;

/**
 * Класс счет для преобразования данных из запроса в форме json
 */
public class Account {
    private String account;

    public Account(String account) {
        this.account = account;
    }

    public String getNumber() {
        return account;
    }

    public void setNumber(String account) {
        this.account = account;
    }
}
