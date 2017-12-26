package utility.apiServices;

import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import utility.services.PropertyReader;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.Map;

import static utility.Log.info;
import static utility.apiServices.ConstantsApi.*;
import static utility.apiServices.HttpRequester.getBody;
import static utility.apiServices.HttpRequester.send;

@Log4j
public class AuthService {

    private String baseUrl;

    public AuthService(String baseUrl) {
        log.info("Auth Service base URL - " + baseUrl);
        this.baseUrl = baseUrl;
    }

    private static PropertyReader reader = new PropertyReader("app.properties");

    public static String getToken(String url) {
        return getAccessToken(initForm(), url);
    }

    public static Form initForm() {
        final Form form = new Form();

        form.param(GRANT_TYPE_KEY, reader.getPropVal(GRANT_TYPE_KEY));
        form.param(CLIENT_ID_KEY, reader.getPropVal(CLIENT_ID_KEY));
        form.param(CLIENT_SECRET_KEY, reader.getPropVal(CLIENT_SECRET_KEY));
        form.param(USER_NAME_KEY, reader.getPropVal(USER_NAME_KEY));
        form.param(USER_PASSWORD_KEY, reader.getPropVal(USER_PASSWORD_KEY));
        form.param(SCOPE_KEY, reader.getPropVal(SCOPE_KEY));

        return form;
    }

    public static String getToken(String url, Map parameters) {
        Form form = new Form();

        fillFormByKeyOrUseDefault(form, parameters, GRANT_TYPE_KEY);
        fillFormByKeyOrUseDefault(form, parameters, CLIENT_ID_KEY);
        fillFormByKeyOrUseDefault(form, parameters, CLIENT_SECRET_KEY);
        fillFormByKeyOrUseDefault(form, parameters, USER_NAME_KEY);
        fillFormByKeyOrUseDefault(form, parameters, USER_PASSWORD_KEY);
        fillFormByKeyOrUseDefault(form, parameters, SCOPE_KEY);

        return getAccessToken(form, url);
    }

    private static Form fillFormByKeyOrUseDefault(Form form, Map<String, String> parameters, String key) {
        String param = parameters.get(key);
        return form.param(key, param != null ?  param : reader.getPropVal(key));
    }

    private static String getAccessToken(Form form, String url) {
        return getAuthData(form, url).getString("access_token");
    }

    /**
     * Retrieves auth info for specified user
     *
     * @param form OAuth params
     * @param url URL of Auth Service
     * @return JSON with access_token, user_id etc.
     */
    public static JSONObject getAuthData(Form form, String url) {
        Entity entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        HttpSampler sampler = new HttpSampler(url, HttpSampler.Method.POST, entity, new MultivaluedHashMap<String, Object>());

        String response = getBody(send(sampler));
        info(response);
        return new JSONObject(response);
    }
}
