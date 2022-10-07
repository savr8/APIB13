package delete;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.PetDeletePojo;
import utils.PayloadUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class Pet {
    @Before
    public void setUp() {
        baseURI = "https://petstore.swagger.io";//alt,Enter
        RestAssured.basePath = "v2/pet";
    }

    @Test
    public void deletePetTest() {
        //post request
        Response response = given().accept(ContentType.JSON)//accept Type
                .contentType(ContentType.JSON)//content Type
                .body(PayloadUtils.getPetPayload())//request body
                .when().post()
                .then().statusCode(200)
                .extract().response();
        Map<String, Object> parsedPost = response.as(new TypeRef<Map<String, Object>>() {
        });
        Assert.assertEquals(20221983,parsedPost.get("id"));


        //TODO add post response validation - validate name,status,id

//get pet request
        response = given().accept(ContentType.JSON)
                .when().get("/20221983")
                .then().statusCode(200).extract().response();
        JsonPath jsonPath = response.jsonPath();
        String names = jsonPath.getString("name");
        Map<String, Object> map = response.as(new TypeRef<Map<String, Object>>() {
        });

        System.out.println(map);


        //TODO add get response validation - validate name,status,id

        //delete pet request

        response = given().accept(ContentType.JSON)
                .when().delete("/20221983")
                .then().statusCode(200)
                .extract().response();
        PetDeletePojo parsedDeletePetResponse = response.as(PetDeletePojo.class);
        Assert.assertEquals(200, parsedDeletePetResponse.getCode());

        response = given().accept(ContentType.JSON)
                .when().get("/20221983")
                .then().statusCode(404)
                .extract().response();

        Map<String, Object> deserialisedGetResponse = response.as(new TypeRef<Map<String, Object>>() {
        });
        String expectedMessage = "Pet not found";
        // String actualMessage = deserialisedGetResponse.get("message").toString();//convert Object to String
        String actualMessage = String.valueOf(deserialisedGetResponse.get("message"));//convert Object to String
        //String actualMessage=(String) deserialisedGetResponse.get("message");//convert Object to String
        Assert.assertEquals(expectedMessage, actualMessage);


    }
}
