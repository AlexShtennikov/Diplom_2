package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserTokenTest {

    private UserApiClient client;
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private LoginApiClient loginClient;
    private String refreshToken;

    @Before
    public void setUp() {

        Faker faker = new Faker();
        client = new UserApiClient();
        loginClient = new LoginApiClient();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().name();
    }

    @After
    public void endSession() {
        loginClient.logout(refreshToken);
        String correctAccessToken = accessToken.replace("Bearer ", "");
        client.deleteUser(correctAccessToken);
    }

    @Test
    @DisplayName("Изменение токена")
    public void validRequestChangeTokenShouldReturnNewAccessToken() {
        final User user = new User(email, password, name);

        accessToken = client.createUser(user)
                .then()
                .statusCode(HTTP_OK)
                .assertThat().body("user.email", equalTo(email))
                .assertThat().body("user.name", equalTo(name))
                .assertThat().body("success", equalTo(true))
                .extract().body().path("accessToken");

        refreshToken = loginClient.loginUser(user)
                .then()
                .statusCode(HTTP_OK)
                .assertThat().body("user.email", equalTo(email))
                .assertThat().body("user.name", equalTo(name))
                .assertThat().body("success", equalTo(true))
                .extract().body().path("refreshToken");

        String actual = loginClient.changeAccessToken(refreshToken)
                .then()
                .statusCode(HTTP_OK)
                .extract().body().path("accessToken");

        String expected = accessToken;
        Assert.assertNotEquals(actual, expected);
    }
}
