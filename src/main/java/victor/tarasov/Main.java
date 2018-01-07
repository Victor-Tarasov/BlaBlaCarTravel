package victor.tarasov;

import victor.tarasov.model.Criteria;
import victor.tarasov.model.trip.RoundTrip;
import victor.tarasov.model.trip.list.response.Coordinates;
import victor.tarasov.service.RoundTripSearcher;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static final Coordinates KRAKOW_COORDINATES = new Coordinates(50.061787f,19.937545f);

    public static void main(String[] args) {
        LocalDateTime departureTime = LocalDateTime.of(2018, 1, 12, 7, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2018, 1, 14, 23, 0);
        Criteria criteria = new Criteria(departureTime, arrivalTime, 24, 200);

        List<RoundTrip> roundTrips = new RoundTripSearcher(KRAKOW_COORDINATES, criteria).findAllTrips();
        new CsvFileView().output(roundTrips);
    }
}
