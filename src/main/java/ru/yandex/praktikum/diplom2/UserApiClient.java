package ru.yandex.praktikum.diplom2;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;

public class UserApiClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static String API_REGISTER = "/api/auth/register";
    private static String API_USER = "/api/auth/user";
    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFilter = new ResponseLoggingFilter();

    public Response createUser(User user){
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(user)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post(API_REGISTER);
    }
    public Response updateUserData(User user, String accessToken){
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .body(user)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .patch(API_USER);
    }

    public Response getUserData(String accessToken){
        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get(API_USER);
    }

    public void deleteUser(String accessToken) {

        RestAssured.with()
                .baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .delete(API_USER)
                .then()
                .statusCode(HTTP_ACCEPTED);
  }
}
