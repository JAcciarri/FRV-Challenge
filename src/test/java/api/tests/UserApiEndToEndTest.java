package api.tests;
import api.models.UserModel;
import api.services.UserApiService;
import api.dataprovider.UserDataProvider;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.LoggerUtil;

import java.util.List;
import java.util.Map;

public class UserApiEndToEndTest {

    private final Logger logger = LoggerUtil.getLogger(UserApiEndToEndTest.class);

    @Test(dataProvider = "userDataSet", dataProviderClass = UserDataProvider.class)
    public void testMultipleUserCreation(UserModel user) {
        SoftAssert sAssert = new SoftAssert();
        UserApiService userService = new UserApiService();

        Response response = userService.createUser(user);
        Assert.assertEquals(response.getStatusCode(), 201, "Status code is not 201 Created");

        // Validaciones del body
        sAssert.assertTrue(response.getBody().asString().contains("id"), "Response body does not contain user id");
        int createdUserId = response.getBody().jsonPath().get("id");
        sAssert.assertEquals(response.jsonPath().getString("name"), user.getName(), "Name mismatch");
        sAssert.assertEquals(response.jsonPath().getString("email"), user.getEmail(), "Email mismatch");
        sAssert.assertEquals(response.jsonPath().getString("status"), user.getStatus(), "Status mismatch");
        logger.info("Usuario creado con ID {}", createdUserId);
        LoggerUtil.logSimpleJSONUser(response.jsonPath().getMap(""), logger);
        sAssert.assertAll();
    }

    @Test
    public void testEndToEndUserFlow() throws InterruptedException {
        SoftAssert sAssert = new SoftAssert();
        UserApiService service = new UserApiService();

        // Crear usuario
        UserModel randomUser = UserDataProvider.generateRandomUser();
        Response responseCreation = service.createUser(randomUser);
        sAssert.assertEquals(responseCreation.statusCode(), 201);
        logger.info("Usuario creado: {}", responseCreation.asString());

        int userId = responseCreation.getBody().jsonPath().get("id");
        Assert.assertNotNull(userId, "El ID del usuario creado es nulo");
        logger.info(String.valueOf(userId));

        // Consultar por ID
        Response fetch =  service.getUserById(userId);
        logger.info(fetch.asString());
        sAssert.assertEquals(fetch.statusCode(), 200, "Status code is not 200 while fetching user by ID");

        Map<String, Object> data = fetch.jsonPath().getMap("");
        sAssert.assertEquals(data.get("name"), randomUser.getName());
        sAssert.assertEquals(data.get("email"), randomUser.getEmail());
        sAssert.assertEquals(data.get("gender"), randomUser.getGender());
        sAssert.assertEquals(data.get("status"), randomUser.getStatus());

        // Ahora verificamos que el usuario creado esta contenido en la lista de usuarios
        Response allUsersResponse = service.getUsers();
        sAssert.assertEquals(allUsersResponse.statusCode(), 200, "Status code is not 200 while getting all users");

        List<Map<String, Object>> allUsers = allUsersResponse.jsonPath().getList("");
        boolean userFound = allUsers.stream()
                .anyMatch(user -> randomUser.getEmail().equals(user.get("email")));

        if(userFound) logger.info("Se encontro el usuario en la lista completa de usuarios");
        sAssert.assertTrue(userFound, "El Usuario creado no se encuentra en la lista de usuarios");
        sAssert.assertAll();
    }


}
