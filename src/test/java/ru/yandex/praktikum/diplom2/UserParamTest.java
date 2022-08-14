package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserParamTest {
    String email;
    String password;
    String name;

    Faker faker = new Faker();

    public UserParamTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] tryCreateUser() {

        Faker faker = new Faker();

        return new Object[][] {
                {faker.internet().emailAddress(), null, faker.internet().password()},
                {null, faker.name().name(), faker.internet().password()},
                {faker.internet().emailAddress(), faker.name().name(), null},
        };
    }

    @Test
    @DisplayName("Попытка создать пользователя без обязательных полей")
    public void createUserWithoutRequiredFieldsShouldReturnError() {

        final User user = new User(email, password, name);

        UserApiClient client = new UserApiClient();

        String actual = client.createUser(user)
                .then()
                .statusCode(403)
                .assertThat().body("success", equalTo(false))
                .extract().body().path("message");

        String expected = "Email, password and name are required fields";
        Assert.assertEquals(actual, expected);
    }
}
