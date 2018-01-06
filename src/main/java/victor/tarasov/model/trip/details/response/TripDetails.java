package victor.tarasov.model.trip.details.response;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import victor.tarasov.model.trip.list.response.Trip;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDetails extends Trip {
    @JsonProperty("stop_overs")
    List<Stop> stopOvers;
}
