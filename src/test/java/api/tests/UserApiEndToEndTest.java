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
        sAssert.assertEquals(response.jsonPath().getString("status"), user.getStatus());
        logger.info("Usuario creado con ID {}", createdUserId);
        LoggerUtil.logSimpleJSONUser(response.jsonPath().getMap(""), logger);
        sAssert.assertAll();
    }

    @Test
    public void testEndToEndUserFlow() {
    }


}
