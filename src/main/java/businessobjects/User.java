package businessobjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utility.services.PropertyReader;

import java.util.Objects;

/**
 * Created by igorp on 19/08/17.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userName;
    private String password;
    private String email;

    public User(String fileLocation) {
        PropertyReader propertyReader = new PropertyReader(fileLocation);
        this.userName = propertyReader.getValue("userName");
        this.password = propertyReader.getValue("password");
        this.email = propertyReader.getValue("email");

        //Generate random email, with +
        String emailDef = propertyReader.getValue("email");
        if (emailDef != null && emailDef.contains("new")) {
            this.email = emailDef.substring(0, emailDef.indexOf("@")) + "+" + String.valueOf(System.currentTimeMillis()) + emailDef.substring(emailDef.indexOf("@"));
        }

    }

    public void refreshEmail() {
        email = email.replaceAll("\\d+", Objects.toString(System.currentTimeMillis()));
    }
}
