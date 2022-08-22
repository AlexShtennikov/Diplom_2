package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class LoginParamTest {

    String email;
    String login;
    String password;

    public LoginParamTest(String email, String login, String password) {
        this.email = email;
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] tryEnterWithLoginAndPassword() {
        Faker faker = new Faker();

        return new Object[][] {
                {faker.internet().emailAddress(), null, faker.internet().password()},
                {faker.internet().emailAddress(), faker.name().name(), null},
                {faker.internet().emailAddress(), faker.name().name(), faker.internet().password()},
        };
    }

    @Test
    @DisplayName("Попытка авторизации с некорректными данными")
    public void createUserWithoutRequiredFieldsShouldReturnError() {

        final User user = new User(email, login, password);

        LoginApiClient client = new LoginApiClient();

        String actual = client.loginUser(user)
                .then()
                .statusCode(HTTP_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .extract().body().path("message");

        String expected = "email or password are incorrect";
        Assert.assertEquals(actual, expected);
    }

}
