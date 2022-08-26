package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class GetUserOrdersTest {

    OrderApiClient orderClient;
    private UserApiClient userClient;
    private LoginApiClient loginClient;
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().name();
        userClient = new UserApiClient();
        loginClient = new LoginApiClient();
    }

    @After
    public void endSession() {
        loginClient.logout(refreshToken);
        String correctAccessToken = accessToken.replace("Bearer ", "");
        userClient.deleteUser(correctAccessToken);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersAuthUserShouldReturnSuccessTrue() {

        orderClient = new OrderApiClient();
        final User user = new User(email, password, name);

        accessToken = userClient.createUser(user)
                .then()
                .statusCode(HTTP_OK)
                .extract().body().path("accessToken");

        refreshToken = loginClient.loginUser(user)
                .then()
                .statusCode(HTTP_OK)
                .extract().body().path("refreshToken");

        String correctAccessToken = accessToken.replace("Bearer ", "");

        orderClient.getUserOrders(correctAccessToken)
                .then().statusCode(HTTP_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersAuthUserShouldReturnSuccessFalse() {

        orderClient = new OrderApiClient();
        final User user = new User(email, password, name);

        accessToken = userClient.createUser(user)
                .then()
                .statusCode(HTTP_OK)
                .extract().body().path("accessToken");

        refreshToken = loginClient.loginUser(user)
                .then()
                .statusCode(HTTP_OK)
                .extract().body().path("refreshToken");

        loginClient.logout(refreshToken);
        String correctAccessToken = "";

        String actual = orderClient.getUserOrders(correctAccessToken)
                .then().statusCode(HTTP_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .extract().body().path("message");

        String expected = "You should be authorised";
        Assert.assertEquals(actual, expected);
    }

}
