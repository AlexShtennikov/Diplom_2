package ru.yandex.praktikum.diplom2;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderApiClient {

    RequestSpecification requestSpec = given()
            .baseUri("https://stellarburgers.nomoreparties.site");

    private static String API_INGREDIENTS = "/api/ingredients";
    private static String API_ORDERS = "/api/orders";
    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFilter = new ResponseLoggingFilter();

    public Response getOrdersLists() {
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .spec(requestSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get(API_INGREDIENTS);
    }

    public Response CreateOrder(Ingredients ordersList, String accessToken) {
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .spec(requestSpec)
                .auth().oauth2(accessToken)
                .body(ordersList)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post(API_ORDERS);
    }

    public Response getUserOrders(String accessToken) {
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .spec(requestSpec)
                .auth().oauth2(accessToken)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get(API_ORDERS);
    }

}
