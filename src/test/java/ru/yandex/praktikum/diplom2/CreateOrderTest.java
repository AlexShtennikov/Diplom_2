package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {

    OrderApiClient orderClient;
    private UserApiClient userClient;
    private LoginApiClient loginClient;
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private String refreshToken;
    private Faker faker;

    @Before
    public void setUp() {
        faker = new Faker();
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
    @DisplayName("Проверка создания заказа с валидными хэш данными под авторизованным пользователем")
    public void createOrdersWithValidValuesAndAuthUserShouldReturnSuccessTrue() {

        orderClient = new OrderApiClient();
        OrdersClass ordersClass = orderClient.getOrdersLists()
                .body().as(OrdersClass.class);

        List<Data> orderList = ordersClass.getOrders();

        //Получим два хэша для теста
        String id = ordersClass.getOrders().get(0).get_id();
        String id2 = ordersClass.getOrders().get(1).get_id();

        List<String> hashList = new ArrayList<>();
        hashList.add(id);
        hashList.add(id2);

        Ingredients ingredients = new Ingredients(hashList);

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

        orderClient.CreateOrder(ingredients, correctAccessToken)
                .then().statusCode(HTTP_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания заказа без хэш данных")
    public void createOrdersWithEmptyValuesAndAuthUserShouldReturnErrorCode400() {

        orderClient = new OrderApiClient();
        List<String> hashList = new ArrayList<>();
        Ingredients ingredients = new Ingredients(hashList);
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

        String actual = orderClient.CreateOrder(ingredients, correctAccessToken)
                .then().statusCode(HTTP_BAD_REQUEST)
                .assertThat().body("success", equalTo(false))
                .extract().body().path("message");

        String expected = "Ingredient ids must be provided";
        Assert.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Проверка создания заказа c некорректыми хэш данными")
    public void createOrdersWithWrongValuesAndAuthUserShouldReturnErrorCode500() {

        orderClient = new OrderApiClient();
        List<String> hashList = new ArrayList<>();
        hashList.add(String.valueOf(faker.internet().hashCode()));
        Ingredients ingredients = new Ingredients(hashList);

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

        orderClient.CreateOrder(ingredients, correctAccessToken)
                .then().statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("Проверка создания заказа под неавторизованным пользователем")
    public void createOrdersWithNoAuthUserShouldReturnErrorCode403() {

        orderClient = new OrderApiClient();
        OrdersClass ordersClass = orderClient.getOrdersLists()
                .body().as(OrdersClass.class);

        List<Data> orderList = ordersClass.getOrders();

        //Получим два хэша для теста
        String id = ordersClass.getOrders().get(0).get_id();
        String id2 = ordersClass.getOrders().get(1).get_id();

        List<String> hashList = new ArrayList<>();
        hashList.add(id);
        hashList.add(id2);

        Ingredients ingredients = new Ingredients(hashList);

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

        orderClient.CreateOrder(ingredients, accessToken)
                .then().statusCode(HTTP_FORBIDDEN)
                .assertThat().body("success", equalTo(false));
    }
}
