package api.dataprovider;

import org.testng.annotations.DataProvider;
import api.models.UserModel;

public class UserDataProvider {

    @DataProvider(name = "userDataSet")
    public Object[][] userDataSet() {
        return new Object[][]{
                {new UserModel("Juan Perez", generateEmail(), "male", "active")},
                {new UserModel("Sofia QA", generateEmail(), "female", "inactive")},
                {new UserModel("Pedro Automation", generateEmail(), "male", "active")},
                {new UserModel("Ana Debug", generateEmail(), "female", "active")},
                {new UserModel("Jose Martinez", generateEmail(), "male", "active")},
        };
    }

    private static String generateEmail() {
        return "Usuario" + System.currentTimeMillis() + Math.random() + "@mail.com";
    }

    public static UserModel generateRandomUser() {
        final long timestamp = System.currentTimeMillis();
        String name = "QA_user" + "_" + timestamp;
        String email = "user" + timestamp + "@mail.com";
        String gender = Math.random() > 0.5 ? "male" : "female";
        String status = Math.random() > 0.5 ? "active" : "inactive";
        return new UserModel(name, email, gender, status);
    }
}