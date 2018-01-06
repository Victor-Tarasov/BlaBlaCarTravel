package victor.tarasov.model.trip.list;

import lombok.Setter;
import lombok.experimental.Accessors;
import victor.tarasov.model.trip.Locale;
import victor.tarasov.model.trip.list.response.Coordinates;
import victor.tarasov.service.DateConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Setter
@Accessors(chain = true)
public class TripListRequest {
    public static final int ESTIMATE_CITY_RADIUS = 30;

    private Locale locale = Locale.POLAND;
    private Coordinates departureCoordinates ;
    private Coordinates arrivalCoordinates;
    private LocalDate earliestDepartureDate;
    private LocalDate latestDepartureDate;
    private Integer earliestDepartureHour;
    private Integer latestDepartureHour;
    private Integer seats = 2;
    private String sort = "trip_price";
    private Integer limit = 500;
    private Integer radiusFrom = ESTIMATE_CITY_RADIUS;
    private Integer radiusTo;
    private Integer page;
    private String fields = "links,departure_date,departure_place,arrival_place,price_with_commission,duration,distance,permanent_id";

    public Map<String, String> toParamsMap() {
        DateConverter converter = new DateConverter();
        ParametersMap params = new ParametersMap();
        params.putNonNull("locale", locale);
        params.putNonNull("fc", departureCoordinates);
        params.putNonNull("db", localDateToString(earliestDepartureDate));
        params.putNonNull("de", localDateToString(latestDepartureDate));
        params.putNonNull("hb", earliestDepartureHour);
        params.putNonNull("he", latestDepartureHour);
        params.putNonNull("seats", seats);
        params.putNonNull("sort", sort);
        params.putNonNull("limit", limit);
        params.putNonNull("radius_from", radiusFrom);
        params.putNonNull("tc", arrivalCoordinates);
        params.putNonNull("radius_to", radiusTo);
        params.putNonNull("fields", fields);
        params.putNonNull("page", page);
        return params;
    }

    public String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private static class ParametersMap extends HashMap<String, String> {

        public void putNonNull(String key, Object value) {
            if (value == null) {
                return;
            }

            super.put(key, String.valueOf(value));
        }
    }
}
