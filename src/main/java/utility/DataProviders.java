package utility;

import businessobjects.User;
import org.testng.annotations.DataProvider;

import java.io.IOException;

import static utility.services.FileReaderService.dataProviderFile;

/**
 * Created by user on 30.05.17.
 */
public class DataProviders {

    @DataProvider(name = "loginMethod")
    public static Object[][] loginMethod() {
        return new Object[][] {{"button"}, {"enter"}};
    }

    @DataProvider(name = "users")
    public static Object[][] users() throws IOException {
        return new Object[][]{
                {new User("properties/defaultUser.properties")},
                {new User("properties/defaultUser.properties")}
        };
    }

    @DataProvider(name = "validEmail")
    public static Object[][] validEmail() throws IOException {
        return dataProviderFile("validEmails.txt");
    }

}
