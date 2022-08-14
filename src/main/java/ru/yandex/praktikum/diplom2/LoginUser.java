package ru.yandex.praktikum.diplom2;

public class LoginUser {

    private String login;
    private String password;

    public LoginUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public LoginUser() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
