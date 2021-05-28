package controller;

/**
 * Класс для парсинга запроса post паспорта
 */
public class Passport {

    private String passport;

    public Passport(String passport) {
        this.passport = passport;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
