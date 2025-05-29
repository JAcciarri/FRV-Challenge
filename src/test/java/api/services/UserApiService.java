package api.services;
import api.models.UserModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class UserApiService {

    // Servicio para encapsular los metodos que se piden de get, post, getAll
    private final String BASE_URL = "https://gorest.co.in/public/v2";
    private final String TOKEN = "Bearer " + System.getenv("API_TOKEN");
    private final Logger logger = LoggerUtil.getLogger(UserApiService.class);

    public UserApiService() {
        RestAssured.baseURI = BASE_URL;
    }

    public Response getUsers() {
        return RestAssured
                .given()
                .get("/users?per_page=100");
    }
    public Response getUserById(int userId) {
        return RestAssured
                .given()
                .get("/users/" + userId);
    }

    public int getExistingUserId(){
        Response response = getUsers();
        if (response.getStatusCode() == 200 && response.jsonPath().getList("id").size() > 0) {
            return response.jsonPath().getInt("[0].id");
        }
        throw new RuntimeException("No users found or failed to retrieve users.");
    }

    public Response createUser(UserModel user) {
        logger.info(TOKEN);
        return RestAssured
                .given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body(user)
                .post("/users");
    }

}
