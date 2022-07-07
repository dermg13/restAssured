package API_Testing.workspaceRestAPI;

import com.sun.xml.bind.v2.model.core.ID;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.core.Is;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK; //Access to status code keywords/values
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class E2E_Project {
    public String path;
    String memberOf = "/workspaces/member-of";
    Map<String, String> variables;
    String Id;
    String user_Id;
    Response response;
    String projectId;
    String projectName;

    String contentType;

    // What is a TestNG annotation that allows us to run a Test Before each Test
    @BeforeTest
    public String setupLogInAndToken() {
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
    public void memberOf() {
        response = RestAssured.given() // creating Response method
                .header("Authorization", setupLogInAndToken())
                .when()
                .get(memberOf)
                .then()
                .log().all()
                .extract().response(); // extracting response Body and start validation tests

        //See the response we are getting
//        System.out.println("-----Printing response body to verify pretty print ------ \n " + response.jsonPath().prettyPrint());

        // Verify status code
        Assert.assertEquals(SC_OK, response.statusCode());
        Assert.assertEquals("Default", response.jsonPath().getString("name[0]"));

        // TODO add tests for ID, userID, Description
//        // ID
//        Assert.assertEquals("4wsOvH0BPG-SisbyqqWo", response.jsonPath().getString("id[0]"));
//        // userId
//        Assert.assertEquals("nAsOvH0BPG-SisbyBKG7",response.jsonPath().getString("userId[0]"));
//        // description
//        Assert.assertEquals("",response.jsonPath().getString("description[0]"));

        // Save the is so it can be used in other requests
        Id = response.jsonPath().get("id[0]");
        // Save the userId so it can be used in other requests
        user_Id = response.jsonPath().get("userId[0]");

        // What can we use to store data as Key and Value
        variables = new HashMap<String, String>();
        variables.put("id", Id);
        variables.put("userId", user_Id);
    }

    @Test(dependsOnMethods = {"memberOf"})
    public void createProject() {
        String requestBody = "{\"id\":\"\",\"created\":\"2021-03-11T06:15:20.845Z\",\"lastModified\":\"2021-03-11T06:15:20.845Z\",\"userId\":\"" + variables.get("userID") + "\",\"workspaceId\":\"" + variables.get("id") + "\",\"name\":\"testing22\",\"description\":\"testing\",\"type\":\"DESIGN\",\"tags\":[]}";

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", setupLogInAndToken())
                .and()
                .body(requestBody)
                .when()
                .post("/design/projects")
                .then()
                .extract()
                .response();

        System.out.println(response.prettyPrint());

        // TODO: TASK Create TestNG Assertions Name, id, userId, workspaceID
        Assert.assertEquals("testing22", response.jsonPath().getString("name"));
        // Using hamcrest Matchers validation
        assertThat(response.jsonPath().getString("name"), is("testing22"));

        // Store id in variable for future use
        projectId = response.jsonPath().get("id");
        System.out.println("New id created when creating a project" + projectId);
    }
    @Test(dependsOnMethods = {"memberOf", "createProject"})
    public void updateProject() {
        // String JSON
        String requestBody1 = "{\"created\":1615443320845,\"description\":\"testing\",\"id\":\"" + projectId + "\",\"lastModified\":1629860121757,\"name\":\"testing Soto\",\"tags\":[],\"type\":\"DESIGN\",\"userId\":\"" + variables.get("userID") + "\",\"workspaceId\":\"" + variables.get("id") + "\"}";

        response = RestAssured.given()
                .headers("Content-Type", "application/json")
                .header("Authorization", setupLogInAndToken())
                .and()
                .body(requestBody1)
                .when()
                .put("/design/projects/" + projectId)
                .then()
                .extract()
                .response();
        System.out.println(response.prettyPeek());

        // TODO HOMEWORK add assertions for: id, name, type, userId, workspaceId, verify status code, Content-type
        Assert.assertEquals(projectId, response.jsonPath().getString("id"));

        // Store id in variable for future use
        projectName = response.jsonPath().get("name");
        Assert.assertEquals(projectName, response.jsonPath().getString("name"));
    }

        @Test (dependsOnMethods = {"memberOf", "createProject"})
        public void verifyContentType_StatusCode() {
            RestAssured.given()
                    .headers("Content-Type", "application/json")
                    .header("Authorization", setupLogInAndToken())
                    .when()
                    .post("https://api.octoperf.com/design/projects?workspaceId=" + projectId)
                    .then()
//                .contentType(ContentType.XML)  ---One way
                    .assertThat()
                    .contentType(ContentType.JSON)
                    .and()
                    .statusCode(201);
        }

    @Test(dependsOnMethods = {"memberOf", "createProject","updateProject"})
    public void deleteProject(){
        response = RestAssured.given()
                .header("Authorization", setupLogInAndToken())
                .when()
                .delete("/design/projects/" + projectId)
                .then()
                .log().all()
                .extract()
                .response();

        // TODO HOMEWORK Validate status code
        assertThat(response.statusCode(),is(204));

    }
}

