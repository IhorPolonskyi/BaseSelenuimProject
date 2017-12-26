package utility.apiServices.services;

import lombok.extern.log4j.Log4j;
import utility.apiServices.HttpClient;
import utility.apiServices.ServiceResponse;
import utility.apiServices.models.example.ExampleModel;
import utility.apiServices.models.example.ExampleProperties;

import javax.ws.rs.core.Response;
import java.util.List;

@Log4j
public class ExampleService {

    private HttpClient httpClient = new HttpClient();

    private String baseUrl;

    public ExampleService(String apiBaseUrl) {
        baseUrl = apiBaseUrl;
    }

    public ServiceResponse getExampleItemInfo(String itemId, boolean withLogging) {
        if(withLogging)
            log.info("Get info about item #" + itemId);

        Response response = httpClient.get(baseUrl + "api/item/" + itemId+"/").getResponse(true);

        return new ServiceResponse(response);
    }

    public String getItemStatus(String itemId){
        return  getExampleItemInfo(itemId, true)
                .asClass(ExampleModel.class)
                .getStatus();
    }

    public String getItemUserId(String itemId){
        return  getExampleItemInfo(itemId, true)
                .asClass(ExampleModel.class)
                .getUserId();
    }

    public List<ExampleProperties> getExampleProperties(String itemId){
        return  getExampleItemInfo(itemId, true)
                .asClass(ExampleModel.class)
                .getServices();
    }
}

