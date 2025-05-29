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
}