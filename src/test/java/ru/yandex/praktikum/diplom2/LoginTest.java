package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginTest {

    private UserApiClient userClient;
    private LoginUserClient loginClient;
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {

        Faker faker = new Faker();
        userClient = new UserApiClient();
        loginClient = new LoginUserClient();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().name();
    }

    @After
    public void endSession() {

        loginClient.logout(refreshToken);

        String correctAccessToken = accessToken.replace("Bearer ", "");
        userClient.deleteUser(correctAccessToken);
    }

    @Test
    @DisplayName("Попытка зайти под существующим пользователем с валидным логином и паролем")
    public void validLoginRequestShouldReturnUserData() {

        final User user = new User(email, password, name);

        accessToken = userClient.createUser(user)
                .then()
                .statusCode(200)
                .assertThat().body("success", equalTo(true))
                .extract().body().path("accessToken");

        refreshToken = loginClient.loginUser(user)
                .then()
                .statusCode(200)
                .assertThat().body("user.email", equalTo(email))
                .assertThat().body("user.name", equalTo(name))
                .assertThat().body("success", equalTo(true))
                .extract().body().path("refreshToken");
    }
}
