package API_Testing.workspaceRestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK; //Access to status code keywords/values

public class E2E_Project {
    public String path;
    String memberOf = "/workspaces/member-of";

    // What is a TestNG annotation that allows us to run a Test Before each Test
    @BeforeTest
    public String setupLogInAndToken(){
        RestAssured.baseURI = "https://api.octoperf.com";
        path = "/public/users/login";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("password", "test123");
        map.put("username", "dermg13@gmail.com");

        return RestAssured.given()
                .queryParams(map)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(path)// send request to end point
                .then()
                .statusCode(SC_OK) // Verify status code = 200 or OK
                .extract() // Method that extracts response JSON data -- everything after will be extracted
                .body() // Body Extracted as JSON format
                .jsonPath() // Navigate using jsonPath -- in the "body" header, navigate in responseBody
                .get("token"); // get value for key Token
    }

    // Write a test for API endpoint member-of
    @Test
    public void verifyToken(){
        Response response = RestAssured.given() // creating Response method
                .header("Authorization", setupLogInAndToken())
                .when()
                .get(memberOf)
                .then()
                .log().all()
                .extract().response(); // extracting response Body and start validation tests

        // Verify status code
        Assert.assertEquals(SC_OK, response.statusCode());
        Assert.assertEquals("Default", response.jsonPath().getString("name[0]"));

        // TODO add tests for ID, userID, Description
        // ID
        Assert.assertEquals("4wsOvH0BPG-SisbyqqWo", response.jsonPath().getString("id[0]"));
        // userId
        Assert.assertEquals("nAsOvH0BPG-SisbyBKG7",response.jsonPath().getString("userId[0]"));
        // description
        Assert.assertEquals("",response.jsonPath().getString("description[0]"));
    }
}
A