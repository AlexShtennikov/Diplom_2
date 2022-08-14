package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserDataTest {

    private UserApiClient userClient;
    private LoginUserClient loginClient;
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private Faker faker;

    @Before
    public void setUp() {

        faker = new Faker();
        userClient = new UserApiClient();
        loginClient = new LoginUserClient();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().name();
    }

    @After
    public void endSession() {
        String correctAccessToken = accessToken.replace("Bearer ", "");
        userClient.deleteUser(correctAccessToken);
    }

    @Test
    @DisplayName("Попытка обновить данные под авторизованным пользователем")
    public void validLoginRequestForAuthorizationUserShouldReturnUserData() {

        final User user = new User(email, password, name);

        //Создаем пользователя
        accessToken = userClient.createUser(user)
                .then()
                .statusCode(200)
                .assertThat().body("success", equalTo(true))
                .extract().body().path("accessToken");

        //Логинимся под пользователем
        String refreshToken = loginClient.loginUser(user)
                .then()
                .statusCode(200)
                .assertThat().body("user.email", equalTo(email))
                .assertThat().body("user.name", equalTo(name))
                .assertThat().body("success", equalTo(true))
                .extract().body().path("refreshToken");

        //Подготавливаем данные для изменения
        String newName = faker.name().name();
        String newEmail = faker.internet().emailAddress();
        final User userUpdate = new User(newEmail, password, newName);

        //Изменяем данные
        String correctAccessToken = accessToken.replace("Bearer ", "");
        userClient.updateUserData(userUpdate, correctAccessToken)
                .then()
                .statusCode(200)
                .assertThat().body("user.email", equalTo(newEmail))
                .assertThat().body("user.name", equalTo(newName))
                .assertThat().body("success", equalTo(true));

        loginClient.logout(refreshToken);
    }

    @Test
    @DisplayName("Попытка обновить данные под неавторизованным пользователем")
    public void validLoginRequestForAuthorizationUserShouldReturnUserData2() {

        final User user = new User(email, password, name);

        //Создаем пользователя
        accessToken = userClient.createUser(user)
                .then()
                .statusCode(200)
                .assertThat().body("success", equalTo(true))
                .extract().body().path("accessToken");

        //Подготавливаем данные для изменения
        String newName = faker.name().name();
        String newEmail = faker.internet().emailAddress();
        final User userUpdate = new User(newEmail, password, newName);

        //Изменяем данные
        String correctAccessToken = accessToken.replace("Bearer ", "");

        //Пробуем менять данные без авторизации
        String actual = userClient.updateUserData(userUpdate, "")
                .then()
                .statusCode(401)
                .extract().body().path("message");

        String expected = "You should be authorised";
        Assert.assertEquals(actual, expected);
    }
}
