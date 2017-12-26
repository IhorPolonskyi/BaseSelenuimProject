package utility.apiServices;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedMap;

public class HttpSampler {
    private String url;
    public enum Method {POST, GET, PUT, DELETE, OPTIONS};
    private Method method;
    Entity entity;
    MultivaluedMap<String, Object> header;
    private boolean randomGenerated;

    public HttpSampler(String url, Method method, Entity entity, MultivaluedMap<String, Object> header) {
        this.url = url;
        this.method = method;
        this.entity = entity;
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public String getMethodName() {
        return method.name();
    }

    public Entity getEntity() {
        return entity;
    }

    public MultivaluedMap<String, Object> getHeader() {
        return header;
    }

    public void setEntity(long count) {
        entity = !randomGenerated ? entity
                : Entity.entity(entity.getEntity()+String.valueOf(count), entity.getMediaType());
    }

    public void setRandomGenerated(boolean randomGenerated) {
        this.randomGenerated = randomGenerated;
    }

    public boolean isRandomGenerated() {
        return randomGenerated;
    }
}
