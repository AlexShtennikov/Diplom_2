package ru.yandex.praktikum.diplom2;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class LoginUserApi {
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

}
