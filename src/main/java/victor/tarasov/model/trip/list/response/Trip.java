
package victor.tarasov.model.trip.list.response;

import lombok.SneakyThrows;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trip {
    private LocalDateTime departureDate;

    @JsonProperty("departure_place")
    private Place departurePlace;

    @JsonProperty("arrival_place")
    private Place arrivalPlace;

    @JsonProperty("price_with_commission")
    private Price price;

    private Integer durationSeconds;

    private Integer distanceKilometers;

    private TripId tripId;

    private String link;

    @JsonProperty("links")
    private void setLink(Map<String, String> links) {
        this.link = links.get("_front");
    }

    @JsonProperty("departure_date")
    private void setDepartureDate(String departureDate) {
        this.departureDate = LocalDateTime.parse(departureDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    @JsonProperty("duration")
    private void setDurationSeconds(Value durationSeconds) {
        this.durationSeconds = extractValue(durationSeconds, "s");
    }

    @JsonProperty("distance")
    private void setDistanceKilometers(Value distanceKilometers) {
        this.distanceKilometers = extractValue(distanceKilometers, "km");
    }

    @SneakyThrows
    @JsonProperty("permanent_id")
    private void setTripId(String permanentId) {
        Matcher matcher = Pattern.compile("(\\d+)-([a-z]+)-(.*)").matcher(permanentId);
        matcher.matches();
        this.tripId = new TripId(Integer.valueOf(matcher.group(1)), matcher.group(2), matcher.group(3));
    }

    private Integer extractValue(Value value, String unit) {
        if (!value.getUnity().equals(unit)) {
            throw new IllegalArgumentException("Wrong value dimensions: " + value.getUnity());
        }

        return value.getValue();
    }

    public LocalDateTime getArrivalTime() {
        return departureDate.plusSeconds(durationSeconds);
    }
}
