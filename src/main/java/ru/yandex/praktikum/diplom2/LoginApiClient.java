package ru.yandex.praktikum.diplom2;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoginApiClient {

    RequestSpecification requestSpec = given()
            .baseUri("https://stellarburgers.nomoreparties.site");

    private final String API_LOGIN = "/api/auth/login";
    private final String API_LOGOUT = "/api/auth/logout";
    private final String API_TOKEN = "/api/auth/token";
    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFilter = new ResponseLoggingFilter();

    public Response loginUser(User user){
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .spec(requestSpec)
                .body(user)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post(API_LOGIN);
    }

    public void logout(String refreshToken) {

        Token token = new Token(refreshToken);

        RestAssured.with()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(token)
                .post(API_LOGOUT)
                .then()
                .statusCode(HTTP_OK);
    }

    public Response changeAccessToken(String refreshToken) {

        Token token = new Token(refreshToken);

        return RestAssured.with()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(token)
                .post(API_TOKEN);
    }

}
