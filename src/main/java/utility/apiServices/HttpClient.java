package utility.apiServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static utility.apiServices.HttpRequester.getHttpClient;

@Log4j
public class HttpClient {

    private String method;
    private String url;
    private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
    private MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();

    public HttpClient() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertResponseToClass(Response response, Class<T> clazz) {
        response.bufferEntity();
        try {
            return response.readEntity(clazz);
        } catch (ProcessingException e) {
            String errorMessage = e + "\nCan't convert response to class " + clazz.getName() + ". Response content: \n" + response.readEntity(String.class);
            throw new RuntimeException(errorMessage);
        } finally {
            response.close();
        }
    }

    /**
     * Convert Response to List of Reviews
     *
     * @param response Response with entity that contains list of reviews
     * @return ArrayList of entities
     * @deprecated
     */
    public static <T> List<T> convertResponseToList(Response response, Class<T> clazz) {
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

    public HttpClient delete(String url) {
        this.url = url;
        this.method = HttpMethod.DELETE;
        this.parameters = null; // Entity must be null for http method GET.
        return this;
    }

    public HttpClient get(String url) {
        this.url = url;
        this.method = HttpMethod.GET;
        this.parameters = null; // Entity must be null for http method GET.
        return this;
    }

    public HttpClient post(String url, MultivaluedMap<String, String> data) {
        this.url = url;
        this.method = HttpMethod.POST;
        parameters = new MultivaluedHashMap<>();
        parameters.putAll(data);

        return this;
    }

    public HttpClient link(String url, String linkUrl) {
        post(url, new Form());
        header("X-HTTP-Method-Override", "LINK");
        header("LINK", linkUrl);

        return this;
    }

    public HttpClient unlink(String url, String unlinkUrl) {
        post(url, new Form());
        header("X-HTTP-Method-Override", "UNLINK");
        header("link", unlinkUrl);

        return this;
    }

    public HttpClient post(String url, Form form) {
        post(url, form.asMap());
        return this;
    }

    public HttpClient post(String url) {
        post(url, new MultivaluedHashMap<>());
        return this;
    }

    public HttpClient put(String url, MultivaluedMap<String, String> data) {
        this.url = url;
        this.method = HttpMethod.PUT;
        parameters = new MultivaluedHashMap<>();
        parameters.putAll(data);

        return this;
    }

    public HttpClient put(String url, Form form) {
        put(url, form.asMap());
        return this;
    }

    public HttpClient put(String url) {
        put(url, new MultivaluedHashMap<>());
        return this;
    }

    @SuppressWarnings("unchecked")
    public HttpClient headers(MultivaluedHashMap headers) {
        this.headers.putAll(headers);
        return this;
    }

    public HttpClient header(String key, String value) {
        headers.add(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public HttpClient parameters(MultivaluedHashMap parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public HttpClient parameter(String key, String value) {
        parameters.add(key, value);
        return this;
    }

    public Response getResponse() {
        try {
            headers.putAll(getDefaultHeaders());
            log.info("Perform " + method + " request to " + url);
            return getHttpClient().target(url).
                    request().
                    headers(headers).
                    build(method, Entity.entity(parameters, (String) headers.getFirst(CONTENT_TYPE))).
                    invoke();
        } finally {
            if (parameters != null) {
                parameters.clear();
            }
            headers.clear();
        }
    }

    public Response getResponse(boolean withLogging) {
        try {
            headers.putAll(getDefaultHeaders());
            if(withLogging)
                log.info("Perform " + method + " request to " + url);
            return getHttpClient().target(url).
                    request().
                    headers(headers).
                    build(method, Entity.entity(parameters, (String) headers.getFirst(CONTENT_TYPE))).
                    invoke();
        } finally {
            if (parameters != null) {
                parameters.clear();
            }
            headers.clear();
        }
    }

    public Object getResponseAsClass(Class clazz) {
        return convertResponseToClass(getResponse(), clazz);
    }

    public String getResponseAsString() {
        return (String) getResponseAsClass(String.class);
    }

    public JSONObject getResponseAsJson() {
        return new JSONObject(getResponseAsString());
    }

    public JSONArray getResponseAsJsonArray() {
        return new JSONArray(getResponseAsString());
    }

    private MultivaluedMap<String, Object> getDefaultHeaders() {
        MultivaluedMap<String, Object> defaultHeaders = new MultivaluedHashMap<>();
        defaultHeaders.add(ACCEPT, APPLICATION_JSON);
        defaultHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);

        return defaultHeaders;
    }

    public HttpClient setAccessToken(String accessToken) {
        return header(HttpHeaders.AUTHORIZATION, accessToken);
    }
}
