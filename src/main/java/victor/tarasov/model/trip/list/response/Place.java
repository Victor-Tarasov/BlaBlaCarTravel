
package victor.tarasov.model.trip.list.response;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Locale;

import static java.util.Locale.ENGLISH;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {
    private String address;
    private String cityName;
    private String country;
    private Coordinates coordinates;

    @JsonCreator
    protected Place(@JsonProperty("address") String address,
                  @JsonProperty("city_name") String cityName,
                  @JsonProperty("latitude") Float latitude,
                  @JsonProperty("longitude") Float longitude,
                  @JsonProperty("country_code") String countryCode) {
        this.address = address;
        this.cityName = cityName;
        this.coordinates = new Coordinates(latitude, longitude);
        this.country = countryCodeToCountry(countryCode);
    }
    private static String countryCodeToCountry(String countryCode) {
        return new Locale("", countryCode).getDisplayCountry(ENGLISH);
    }
}
