package ru.yandex.praktikum.diplom2;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class OrderApiClient {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFilter = new ResponseLoggingFilter();

    public Response getOrdersLists() {
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get("/api/ingredients");
    }

    public Response CreateOrder(Ingredients ordersList, String accessToken) {
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .body(ordersList)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/api/orders");
    }

    public Response getUserOrders(String accessToken) {
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get("/api/orders");
    }

}
