package utility.apiServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ServiceResponse {

    private Response response;

    public ServiceResponse(Response response) {
        response.bufferEntity();
        this.response = response;

    }

    public <T> List<T> asList(Class<T> clazz) {
        List<T> reviewsList = new ArrayList<>();
        String responseAsString = response.readEntity(String.class);

        try {
            JSONArray reviewsJsonArray = new JSONArray(responseAsString);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            for (int i = 0; i < reviewsJsonArray.length(); i++) {
                JSONObject reviewObject = (JSONObject) reviewsJsonArray.get(i);
                reviewsList.add(mapper.readValue(reviewObject.toString(), clazz));
            }
        } catch (Exception e) {
            log.error("Error while processing response");
            throw new RuntimeException(responseAsString, e);
        }
        return reviewsList;
    }

    public <T> T asClass(Class<T> clazz) {
        try {
            return response.readEntity(clazz);
        } catch (ProcessingException e) {
            e.printStackTrace();
            String errorMessage = e + "\nCan't convert response to class " + clazz.getName() + ". Response content: \n" + response.readEntity(String.class);
            throw new RuntimeException(errorMessage);
        }
    }

    public String asString() {
        return (String) asClass(String.class);
    }

    public JSONObject asJson() {
        return new JSONObject(asString());
    }

    public JSONArray asJsonArray() {
        try {
            return new JSONArray(asString());
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to convert response to JSON array.\nActual content is:\n" + asString());
        }
    }

    public Response asRawResponse() {
        return response;
    }
}
