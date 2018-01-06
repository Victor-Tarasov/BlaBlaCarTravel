
package victor.tarasov.model.trip.list.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import lombok.Getter;
import victor.tarasov.model.trip.list.response.Pager;
import victor.tarasov.model.trip.list.response.Trip;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripListResponse {
    @JsonProperty("pager")
    private Pager pager;

    @JsonProperty("trips")
    private List<Trip> trips = new ArrayList<Trip>();

    @JsonProperty("top_trips")
    private void addTrips(List<Trip> topTrips) {
        this.trips.addAll(topTrips);
    }
}
