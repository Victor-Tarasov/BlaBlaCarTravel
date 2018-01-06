package victor.tarasov.model.trip.details.response;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import victor.tarasov.model.trip.list.response.Place;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stop extends Place {
    boolean isDeparturePlace;
    boolean isArrivalPlace;

    @JsonCreator
    Stop(@JsonProperty("address") String address,
                  @JsonProperty("city_name") String cityName,
                  @JsonProperty("latitude") Float latitude,
                  @JsonProperty("longitude") Float longitude,
                  @JsonProperty("country_code") String countryCode,
                  @JsonProperty("arrival_place") boolean isArrivalPlace,
                  @JsonProperty("departure_place") boolean isDeparturePlace) {
        super(address, cityName, latitude, longitude, countryCode);
        this.isArrivalPlace = isArrivalPlace;
        this.isDeparturePlace = isDeparturePlace;
    }
}
