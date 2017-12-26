package utility.apiServices.models.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(value = "_links", ignoreUnknown = true)
public class ExampleProperties {

    private int id;
}
