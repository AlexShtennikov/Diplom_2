package ru.yandex.praktikum.diplom2;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class LoginApiClient {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFilter = new ResponseLoggingFilter();

    public Response loginUser(User user){
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(user)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/api/auth/login");
    }

    public void logout(String refreshToken) {

        Token token = new Token(refreshToken);

        RestAssured.with()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(token)
                .post("/api/auth/logout")
                .then()
                .statusCode(200);
    }

    public Response changeAccessToken(String refreshToken) {

        Token token = new Token(refreshToken);

        return RestAssured.with()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(token)
                .post("/api/auth/token");
    }

}
