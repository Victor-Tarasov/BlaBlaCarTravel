package victor.tarasov.model.trip;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import victor.tarasov.model.trip.list.response.Price;
import victor.tarasov.model.trip.list.response.Trip;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.HOURS;

@Getter
@AllArgsConstructor
public class RoundTrip {
    public static final String CSV_DECIMETER = ",";
    private Trip tripToDestination;
    private Trip returnTrip;

    public static String header() {
        return Lists.newArrayList(
                "Country",
                "City",
                "Total price",
                "Price for forward trip",
                "Price for return trip",
                "Hours at destination",
                "Departure time",
                "Arrival to destination time",
                "Departure from destination time",
                "Arial time",
                "Distance",
                "Link for forward travel",
                "Link for return travel"
        ).stream().collect(Collectors.joining(CSV_DECIMETER));
    }

    public String toCsv() {
        return Lists.newArrayList(
                getDestinationCountry(),
                getDestinationCity(),
                getTotalPrice(),
                getPriceToDestination(),
                getReturnTripPrice(),
                getHourAtDestination(),
                getDepartureTime(),
                getArrivalToDestinationTime(),
                getDepartureBackTime(),
                getArrivalBackTime(),
                getDistance(),
                getLinkForForwardTravel(),
                getLinkForReturnTravel()
        ).stream()
                .map(String::valueOf)
                .map(value -> value.replaceAll(",", ""))
                .collect(Collectors.joining(CSV_DECIMETER));
    }

    public String getDestinationCountry() {
        return tripToDestination.getArrivalPlace().getCountry();
    }

    public String getDestinationCity() {
        return tripToDestination.getArrivalPlace().getCityName();
    }

    public Integer getTotalPrice() {
        return getPriceToDestination() + getReturnTripPrice();
    }

    private Integer extractPrice(Trip trip) {
        Price price = trip.getPrice();
        String currency = price.getCurrency();
        if (!currency.equals("PLN")) {
            throw new IllegalArgumentException("Wrong currency: " + currency);
        }
        return price.getValue();
    }

    public Integer getPriceToDestination() {
        return extractPrice(tripToDestination);
    }

    public Integer getReturnTripPrice() {
        return extractPrice(returnTrip);
    }

    public Long getHourAtDestination() {
        return tripToDestination.getArrivalTime().until(returnTrip.getDepartureDate(), HOURS);
    }

    public LocalDateTime getDepartureTime() {
        return tripToDestination.getDepartureDate();
    }

    public LocalDateTime getArrivalToDestinationTime() {
        return tripToDestination.getArrivalTime();
    }

    public LocalDateTime getDepartureBackTime() {
        return returnTrip.getDepartureDate();
    }

    public LocalDateTime getArrivalBackTime() {
        return returnTrip.getArrivalTime();
    }

    public Integer getDistance() {
        return tripToDestination.getDistanceKilometers();
    }

    public String getLinkForForwardTravel() {
        return tripToDestination.getLink();
    }

    public String getLinkForReturnTravel() {
        return returnTrip.getLink();
    }
}
