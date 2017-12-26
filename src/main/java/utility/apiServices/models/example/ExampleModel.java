package utility.apiServices.models.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import utility.apiServices.models.example.ExampleProperties;

import java.util.List;

@Data
@JsonIgnoreProperties(value = {"_links"}, ignoreUnknown = true)
public class ExampleModel {

    @JsonProperty(value = "USER_ID")
    public String userId;

    public String status;

    public List<ExampleProperties> services;
}
