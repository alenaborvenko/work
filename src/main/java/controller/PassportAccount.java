package controller;

/**
 * Класс для парсинга post запроса где передается паспорт и счет
 */
public class PassportAccount {
    private String passport;
    private String account;

    public PassportAccount(String passport, String account) {
        this.passport = passport;
        this.account = account;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
