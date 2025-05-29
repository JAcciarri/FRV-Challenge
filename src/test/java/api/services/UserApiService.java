package api.services;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UserApiService {

    // Servicio para encapsular los metodos que se piden de get, post, getAll
    private final String BASE_URL = "https://gorest.co.in/public/v2";
    private final String TOKEN = "Bearer " + System.getenv("API_TOKEN");

    public UserApiService() {
        RestAssured.baseURI = BASE_URL;
    }

    public Response getUsers() {
        return RestAssured
                .given()
                .get("/users");
    }
    public Response getUserById(int userId) {
        return RestAssured
                .given()
                .get("/users/" + userId);
    }

    public int getExistingUserId(){
        Response response = getUsers();
        if (response.getStatusCode() == 200 && response.jsonPath().getList("id").size() > 0) {
            return (int) response.jsonPath().getList("id").get(1);
        }
        throw new RuntimeException("No users found or failed to retrieve users.");
    }

}
