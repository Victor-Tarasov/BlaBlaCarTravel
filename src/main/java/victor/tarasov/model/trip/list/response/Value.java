
package victor.tarasov.model.trip.list.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {
    @JsonProperty("value")
    private Integer value;

    @JsonProperty("unity")
    private String unity;
}
