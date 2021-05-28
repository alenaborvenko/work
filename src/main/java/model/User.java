package model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * класс пользователь
 */
public class User {
    private int id;
    private String name;
    private String surname;
    private String passport;

    public User(int id, String name, String fio, String passport) {
        this.id = id;
        this.name = name;
        this.surname = fio;
        this.passport = passport;
    }

    public String getPassport() {
        return passport;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(passport, user.passport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, passport);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("surname='" + surname + "'")
                .add("passport='" + passport + "'")
                .toString();
    }
}
