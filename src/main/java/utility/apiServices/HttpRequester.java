package utility.apiServices;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Optional;

import static utility.apiServices.AuthService.getToken;

public class HttpRequester {

    public enum HttpEntry {
        BODY,
        HEADER;
    };

    /**
     * Get HTTP client that do not validate HTTPS certificates.
     *
     * @return HTTP client to perform requests
     */
    public static Client getHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            return ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier((s, sslSession) -> true).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to init HTTP client. " + e.getMessage());
        }
    }

    public static Response send(HttpSampler sampler){
        Client client = getHttpClient();

        return client.target(sampler.getUrl())
                .request()
                .headers(sampler.getHeader())
                .build(sampler.getMethodName(), sampler.getEntity()).invoke();
    }

    public static String getBody(Response response){
        return response.readEntity(String.class);
    }

    public static HttpSampler initSampler(String url, String info) {

        String[] args = parseValues(info.split("-->"));

        HttpSampler sampler = new HttpSampler(url + args[1],
                HttpSampler.Method.valueOf(args[0]), parseBody(args), parseHeader(args));

        sampler.setRandomGenerated(isBodyRandomGenerated(args));
        return sampler;
    }

    private static boolean isBodyRandomGenerated(String... args) {
        return Arrays.stream(args)
                .filter(arg -> arg.contains("body"))
                .anyMatch(arg -> arg.contains("$PART"));
    }

    private static Entity parseBody(String... args) {
        return Arrays.stream(args)
                .filter(arg -> arg.contains("body"))
                .map(arg -> arg.split(":"))
                .map(entry -> Entity.entity(entry[1], MediaType.APPLICATION_FORM_URLENCODED_TYPE))
                .findAny()
                .orElse(null);
    }

    private static MultivaluedMap parseHeader(String... args) {
        return parseHttpEntry(HttpEntry.HEADER, args)
                .map(e -> e.split("&"))
                .map(e -> {
                    MultivaluedMap<String, Object> headers = new MultivaluedHashMap();
                    Arrays.stream(e)
                            .map(item -> item.split("="))
                            .forEach(entry -> {
                                headers.put(entry[0], Arrays.asList(entry[1]));
                            });
                    return headers;

                }).orElse(new MultivaluedHashMap());
    }

    private static Optional<String> parseHttpEntry(HttpEntry entry, String... args) {
        return Arrays.stream(args)
                .filter(arg -> arg.contains(entry.name().toLowerCase()))
                .map(arg -> arg.split(":"))
                .map(items -> items[1])
                .findAny();
    }

    private static String parseValues(String url, String data) {
        data = data.contains("$PART") ? data.replace("$PART", String.valueOf(System.currentTimeMillis())) : data;
        data = data.contains("$ACCESS_TOKEN") ? data.replace("$ACCESS_TOKEN", getToken(url + "oauth/token")) : data;
        return data;
    }

    private static String[] parseValues(String ... data) {
        return Arrays.stream(data)
                .map(HttpRequester::parseValues).toArray(String[]::new);
    }
}
