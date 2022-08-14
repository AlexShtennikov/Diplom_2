package ru.yandex.praktikum.diplom2;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UserApiClient {
    public static final String BASE_URL = " https://stellarburgers.nomoreparties.site";
    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFilter = new ResponseLoggingFilter();

    public Response createUser(User user){
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .baseUri(BASE_URL)
                .body(user)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/api/auth/register");
    }

//    public void deleteUser(String login, String password) {
//
//        LoginCourierApiClient loginClient = new LoginCourierApiClient();
//        CourierApiClient client = new CourierApiClient();
//        final LoginCourier loginCourier = new LoginCourier(login, password);
//
//        int id = loginClient.loginCourier(loginCourier)
//                .then()
//                .statusCode(200)
//                .extract().body().path("id");
//
//        RestAssured.with()
//                .baseUri(client.BASE_URL)
//                .contentType(ContentType.JSON)
//                .delete("/api/v1/courier/{id}", id)
//                .then()
//                .statusCode(200);
//    }
}
