package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserTest {
    private UserApiClient client;
    private String email;
    private String password;
    private String name;
    private String accessToken;

    @Before
    public void setUp() {

        Faker faker = new Faker();
        client = new UserApiClient();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().name();
    }

    @After
    public void endSession() {
        String correctAccessToken = accessToken.replace("Bearer ", "");
        client.deleteUser(correctAccessToken);
    }

    @Test
    @DisplayName("Создание пользователя")
    public void validCreateRequestShouldReturnUserWithGivenParams() {
        final User user = new User(email, password, name);

        accessToken = client.createUser(user)
                .then()
                .statusCode(200)
                .assertThat().body("user.email", equalTo(email))
                .assertThat().body("user.name", equalTo(name))
                .assertThat().body("success", equalTo(true))
                .extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Попытка создать существующего пользователя")
    public void validCreateWithExistingUserShouldReturnError() {

        final User user = new User(email, password, name);

        accessToken = client.createUser(user)
                .then()
                .statusCode(200)
                .assertThat().body("success", equalTo(true))
                .extract().body().path("accessToken");

        String actual = client.createUser(user)
                .then()
                .statusCode(403)
                .assertThat().body("success", equalTo(false))
                .extract().body().path("message");

        String expected = "User already exists";
        Assert.assertEquals(actual, expected);
    }

}