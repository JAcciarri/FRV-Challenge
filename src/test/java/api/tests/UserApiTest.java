package api.tests;

import api.services.UserApiService;
import io.restassured.response.Response;
import org.slf4j.Logger;
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
        userDetail.jsonPath().getMap("").forEach(
                (key, value) -> logger.info("User {}: {}", key, value)
        );
        sAssert.assertAll();

    }
    @Test
    public void testGetAllUsers(){
        SoftAssert sAssert = new SoftAssert();
        UserApiService userService = new UserApiService();
        Response response = userService.getUsers();
        sAssert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");
        System.out.println(response.getBody().asString());
        sAssert.assertTrue(response.getBody().asString().contains("id"), "Response body does not contain any user id");
        sAssert.assertTrue(response.getBody().asString().contains("name"), "Response body does not contain any user name");
        sAssert.assertTrue(response.getBody().asString().contains("email"), "Response body does not contain any user email");
        List<Map<String, Object>> users = response.jsonPath().getList("");
        LoggerUtil.printAPIListResponse(users, logger);
        sAssert.assertAll();
    }

}
