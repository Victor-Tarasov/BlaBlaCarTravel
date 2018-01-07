package victor.tarasov.service;

import victor.tarasov.model.Criteria;
import victor.tarasov.model.trip.list.TripListRequest;
import victor.tarasov.model.trip.list.response.Coordinates;
import victor.tarasov.model.trip.list.response.Trip;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static victor.tarasov.model.trip.list.TripListRequest.ESTIMATE_CITY_RADIUS;
import static victor.tarasov.Main.SEATS_NEEDED;

public class ForwardTripSearcher extends AbstractTripSearcher {
    private BalkOperationExecutor balkOperationExecutor = new BalkOperationExecutor();
    private Coordinates from;

    public ForwardTripSearcher(Coordinates from, Criteria criteria) {
        super(criteria);
        this.from = from;
    }

    @Override
    public List<Trip> findAllTrips() {
        List<Trip> trips = listAllTrips();
        System.out.println("Number of full trips: " + trips.size());
        trips.addAll(finAllStopOvers(trips));
        System.out.println("Total number of trips " + trips.size());
        List<Trip> filteredTrips = trips.stream()
                .filter(this::isTripsArrivalTimeValid)
                .filter(this::isPriceValid)
                .filter(this::isTripDistanceValid)
                .filter(this::isSeatsValid)
                .collect(Collectors.toList());
        System.out.println("Total number of trips after filtration: " + filteredTrips.size());
        return filteredTrips;
    }

    private List<Trip> listAllTrips() {
        List<Trip> trips = extractTripsFromAllPages(makeTripListRequest().setSeats(null));
        return trips.stream()
                .filter(this::isTripsDepartureTimeValid)
                .filter(this::isTripDistanceValid)
                .filter(this::isPriceValid)
                .collect(Collectors.toList());
    }

    private List<Trip> finAllStopOvers(List<Trip> trips) {
        List<Trip> longDistanceTrips = trips.parallelStream()
                .filter(trip -> trip.getDistanceKilometers() > 80)
                .collect(Collectors.toList());
        System.out.println("Find stop overs for trips.");
        List<Trip> stopOverTrips = balkOperationExecutor.runBalkOperation(longDistanceTrips, this::findStopOvers)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println("Stop over trips: " + trips.size());
        return stopOverTrips;
    }

    private List<Trip> findStopOvers(Trip trip) {
        return new StopOverSearcher(trip).findAllTrips();
    }

    private boolean isSeatsValid(Trip trip) {
        return trip.getSeats() > SEATS_NEEDED;
    }

    private boolean isTripDistanceValid(Trip trip) {
        return trip.getDistanceKilometers() >= ESTIMATE_CITY_RADIUS;
    }

    private boolean isPriceValid(Trip trip) {
        return trip.getPrice().getValue() <= criteria.getPrice();
    }

    private boolean isTripsArrivalTimeValid(Trip trip) {
        LocalDateTime arrivalTime =  trip.getDepartureDate().plusSeconds(trip.getDurationSeconds());
        LocalDateTime latestDepartureTime = calculateLatestArrivalTime(trip);
        return arrivalTime.isBefore(latestDepartureTime) || arrivalTime.isEqual(latestDepartureTime);
    }

    protected LocalDateTime calculateLatestArrivalTime(Trip trip) {
        return criteria.getLatestArrivalTime()
                .minusSeconds(trip.getDurationSeconds())
                .minusHours(criteria.getHoursAtDestination());
    }

    private boolean isTripsDepartureTimeValid(Trip trip) {
        LocalDateTime departureTime = trip.getDepartureDate();
        return departureTime.isAfter(criteria.getEarliestDepartureTime())
                || departureTime.isEqual(criteria.getEarliestDepartureTime());
    }

    private TripListRequest makeTripListRequest() {
        return new TripListRequest()
                .setDepartureCoordinates(from)
                .setEarliestDepartureDate(criteria.getEarliestDepartureTime().toLocalDate())
                .setLatestDepartureDate(criteria.getLatestArrivalTime().toLocalDate());
    }
}
