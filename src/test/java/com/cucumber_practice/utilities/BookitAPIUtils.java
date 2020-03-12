package com.cucumber_practice.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
public class BookitAPIUtils {

    public static String generateToken(String email,String password){

        Response response = given().accept(ContentType.JSON)
                .queryParam("email", email)
                .queryParam("password", password)
                .when().get(ConfigurationReader.get("qa1api.uri") + "/sign");

        //get token from api and save inside the String variable
        String token = response.path("accessToken");
        System.out.println("token = " + token);
        return token;
    }


    public static void main(String[] args) {
        generateToken("sbirdbj@fc2.com","asenorval");
    }
}