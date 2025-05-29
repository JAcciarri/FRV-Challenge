package api.tests;

import api.models.UserModel;
import api.services.UserApiService;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.LoggerUtil;

import java.util.List;
import java.util.Map;


public class UserApiTest {

    Logger logger = LoggerUtil.getLogger(UserApiTest.class);

    // Podria implementar un data provider para pasar los datos de los usuarios
    @Test
    public void testGetUserById(){
        SoftAssert sAssert = new SoftAssert();
        UserApiService userService = new UserApiService();
        final int userId = userService.getExistingUserId();

        Response userDetail = userService.getUserById(userId);
        sAssert.assertEquals(userDetail.getStatusCode(), 200, "Status code is not 200");
        sAssert.assertTrue(userDetail.getBody().asString().contains("id"), "Response body does not contain user id");
        sAssert.assertEquals((int)userDetail.getBody().jsonPath().get("id"), userId, "User id does not match");
        sAssert.assertTrue(userDetail.getBody().asString().contains("name"), "Response body does not contain user name");
        sAssert.assertTrue(userDetail.getBody().asString().contains("email"), "Response body does not contain user email");
        // Listar el detalle especifico del usuario
        logger.info("User details in JSON: {}", userDetail.getBody().asString());
        logSimpleJSONUser(userDetail);
        sAssert.assertAll();

    }
    @Test
    public void testGetAllUsers(){
        SoftAssert sAssert = new SoftAssert();
        UserApiService userService = new UserApiService();
        Response response = userService.getUsers();
        sAssert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");
        sAssert.assertTrue(response.getBody().asString().contains("id"), "Response body does not contain any user id");
        sAssert.assertTrue(response.getBody().asString().contains("name"), "Response body does not contain any user name");
        sAssert.assertTrue(response.getBody().asString().contains("email"), "Response body does not contain any user email");
        List<Map<String, Object>> users = response.jsonPath().getList("");
        LoggerUtil.printAPIListResponse(users, logger);
        sAssert.assertAll();
    }

    @Test
    public void createUser() {
        SoftAssert sAssert = new SoftAssert();
        UserApiService userService = new UserApiService();

        // Crear un nuevo usuario
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@mail.com";
        UserModel newUser = new UserModel("QA Automation", uniqueEmail, "male", "active");
        Response response = userService.createUser(newUser);
        // Uso un hard assert porque si no devuelve 201 entonces el test completo debe fallar
        Assert.assertEquals(response.getStatusCode(), 201, "Status code is not 201 Created");

        // Validaciones del body
        sAssert.assertTrue(response.getBody().asString().contains("id"), "Response body does not contain user id");
        int createdUserId = response.getBody().jsonPath().get("id");
        sAssert.assertEquals(response.jsonPath().getString("name"), newUser.getName(), "Name mismatch");
        sAssert.assertEquals(response.jsonPath().getString("email"), newUser.getEmail(), "Email mismatch");
        sAssert.assertEquals(response.jsonPath().getString("gender"), newUser.getGender(), "Gender mismatch");
        sAssert.assertEquals(response.jsonPath().getString("status"), newUser.getStatus(), "Status mismatch");

        logger.info("Usuario creado exitosamente con ID: {}", createdUserId);
        logSimpleJSONUser(response);

        sAssert.assertAll();
    }

    private void logSimpleJSONUser(Response res){
        res.jsonPath().getMap("").forEach(
                (key, value) -> logger.info("User {}: {}", key, value)
        );
    }


}
