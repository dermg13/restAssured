package API_Testing.StudentPractice;

import Pojos.loginPojos;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class loginWithPojosData {
    @Test
    public void logInWithPojosData(){
        loginPojos data = new loginPojos();
        data.setPassWord("test123");
        data.setUserName("dermg13@gmail.com");

        RestAssured.baseURI = "https://api.octoperf.com";
        String path = "/public/users/login";

        RestAssured.given()
                .queryParam("username", data.getUserName())
                .queryParam("password", data.getPassWord())
                .when()
                .post(path)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .log().all();
    }
}
