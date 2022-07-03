package API_Testing.API_Day2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

public class _02_LoginTypes {
    
    /*
Log in with Full URL with query params
and verify status code and Content-type is equal to JSON
 */
    @BeforeTest
    public String setupLogInAndToken(){
        RestAssured.baseURI = "https://api.octoperf.com";
        String path = "/public/users/login";

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
                .extract() // Method that extracts response JSON data
                .body() // Body Extracted as JSON format
                .jsonPath() // Navigate using jsonPath
                .get("token"); // get value for key Token
    }
    
    @Test
    public void testUsingQueryParms(){
        RestAssured.given()
                .when()
                .post("https://api.octoperf.com/public/users/login?password=test123&username=dermg13@gmail.com")
                .then()
                .assertThat().statusCode(200)
                .and()
                .assertThat().contentType(ContentType.JSON);
    }
    /*
    Log in using Map to verify Content Type
    Map => Stores Key and Value -> Hashmap implements Map, we can store different data Type of Object
     */

    @Test
    public void LogInWithMap(){
        RestAssured.baseURI="https://api.octoperf.com";
        String path = "/public/users/login";
        // WRITE A MAP
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "dermg13@gmail.com");
        map.put("password", "test123");

        RestAssured.given()
                .queryParams(map)
                .when()
                .post(path)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .and()
                .assertThat()
                .statusCode(200);
    }

    // Using query Param
    @Test
    public void LogInWithQueryParam(){
        RestAssured.baseURI="https://api.octoperf.com";
        String path = "/public/users/login";

        RestAssured.given()
                .queryParam("username","tla.jiraone@gmail.com")
                .queryParam("password","test12")
                .when()
                .post(path)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .and()
                .assertThat()
                .statusCode(200);
    }
}
