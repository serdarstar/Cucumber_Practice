package com.cucumber_practice.step_definitions;

import com.cucumber_practice.pages.SelfPage;
import com.cucumber_practice.utilities.BookitAPIUtils;
import com.cucumber_practice.utilities.ConfigurationReader;
import com.cucumber_practice.utilities.DBUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;
public class ApiStepDefs {


    String token;
    Response response;
    String emailGlobal;
    @Given("I logged Bookit api using {string} and {string}")
    public void i_logged_Bookit_api_using_and(String email, String password) {

        token = BookitAPIUtils.generateToken(email,password);
        //gettin value from feautre file and assign to global variable
        emailGlobal=email;

    }

    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {

        //url for api
        String url = ConfigurationReader.get("qa1api.uri")+"/api/users/me";

        response = given().accept(ContentType.JSON)
                .header("Authorization", token)
                .when().get(url);


    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {

        //verify status code is matching with feature file
        Assert.assertEquals(response.statusCode(),statusCode);

    }

    @Then("the information about current user from api and database should be match")
    public void the_information_about_current_user_from_api_and_database_should_be_match() {

        //GETTING INFORMATION FROM DATABASE
        //query for user information
        String query ="select id,firstname,lastname,role from users\n" +
                "where email = '"+emailGlobal+"';";

        Map<String, Object> dbInfoMap = DBUtils.getRowMap(query);
        System.out.println("dbInfoMap = " + dbInfoMap);

        long expectedId = (long) dbInfoMap.get("id");
        String expectedFirstname = (String) dbInfoMap.get("firstname");
        String expectedLastname = (String) dbInfoMap.get("lastname");
        String expectedRole = (String) dbInfoMap.get("role");


        //GETTING INFORMATION FROM API
        JsonPath json = response.jsonPath();

        long actualId = json.getLong("id");
        String actualFirstname = json.getString("firstName");
        String actualLastname= json.getString("lastName");
        String actualRole = json.getString("role");


        //VERIFY DATABASE AND API VALUES
        Assert.assertEquals(actualId,expectedId);
        Assert.assertEquals(actualFirstname,expectedFirstname);
        Assert.assertEquals(actualLastname,expectedLastname);
        Assert.assertEquals(actualRole,expectedRole);



    }

    @Then("UI,API and Database user information must be match")
    public void ui_API_and_Database_user_information_must_be_match() {

        //GETTING INFORMATION FROM DATABASE
        //query for user information
        String query ="select id,firstname,lastname,role from users\n" +
                "where email = '"+emailGlobal+"';";

        Map<String, Object> dbInfoMap = DBUtils.getRowMap(query);
        System.out.println("dbInfoMap = " + dbInfoMap);

        long expectedId = (long) dbInfoMap.get("id");
        String expectedFirstname = (String) dbInfoMap.get("firstname");
        String expectedLastname = (String) dbInfoMap.get("lastname");
        String expectedRole = (String) dbInfoMap.get("role");

        //GETTING INFORMATION FROM API
        JsonPath json = response.jsonPath();

        long actualId = json.getLong("id");
        String actualFirstname = json.getString("firstName");
        String actualLastname= json.getString("lastName");
        String actualRole = json.getString("role");

        //GETTING INFORMATION FROM UI
        SelfPage selfPage = new SelfPage();
        String actualFullnameUI = selfPage.name.getText();
        String actualRoleUI = selfPage.role.getText();

        //verify db vs ui
        String expectedFullname = expectedFirstname+" "+expectedLastname;
        //verfiy fullnames db vs ui
        Assert.assertEquals(actualFullnameUI,expectedFullname);

        //verify ui vs api (role)
        Assert.assertEquals(actualRoleUI,actualRole);

    }



}
