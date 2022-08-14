package ru.yandex.praktikum.diplom2;

import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private UserApiClient client;
    private String email;
    private String password;
    private String name;

    @Before
    public void setUp() {

        Faker faker = new Faker();
        client = new UserApiClient();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().name();
    }

    @Test
    public void test1() {
        final User user = new User(email, password, name);

//        boolean result = client.createUser(user)
//                .then()
//                .statusCode(201)
//                .extract().body().path("ok");

       // Assert.assertTrue(result);
    }

}