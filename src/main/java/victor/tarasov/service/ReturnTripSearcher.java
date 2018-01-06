package victor.tarasov.service;

import victor.tarasov.model.Criteria;
import victor.tarasov.model.trip.list.TripListRequest;
import victor.tarasov.model.trip.list.response.Coordinates;
import victor.tarasov.model.trip.list.response.Trip;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static victor.tarasov.model.trip.list.TripListRequest.ESTIMATE_CITY_RADIUS;

public class ReturnTripSearcher extends AbstractTripSearcher {
    private Trip tripToDestination;
    private Coordinates to;

    public ReturnTripSearcher(Trip tripToDestination, Coordinates to, Criteria criteria) {
        super(criteria);
        this.tripToDestination = tripToDestination;
        this.to = to;
    }

    @Override
    public List<Trip> findAllTrips() {
        return extractTripsFromAllPages(makeRequest()).stream()
                .filter(this::isDepartureTimeValid)
                .filter(this::isArrivalTimeValid)
                .filter(this::isPriceValid)
                .collect(Collectors.toList());
    }

    private boolean isPriceValid(Trip trip) {
        return trip.getPrice().getValue() + tripToDestination.getPrice().getValue() <= criteria.getPrice();
    }

    private boolean isDepartureTimeValid(Trip trip) {
        LocalDateTime earliestDepartureTime = calculateEarliestDepartureTime();
        LocalDateTime departureTime = trip.getDepartureDate();
        return earliestDepartureTime.isBefore(departureTime) || earliestDepartureTime.isEqual(departureTime);
    }

    private boolean isArrivalTimeValid(Trip trip) {
        LocalDateTime arrivalTime = trip.getArrivalTime();
        LocalDateTime latestArrivalTime = criteria.getLatestArrivalTime();
        return arrivalTime.isBefore(latestArrivalTime) || arrivalTime.isEqual(latestArrivalTime);
    }

    private TripListRequest makeRequest() {
        Coordinates departureCoordinates = tripToDestination.getArrivalPlace().getCoordinates();
        return new TripListRequest()
                .setDepartureCoordinates(departureCoordinates)
                .setArrivalCoordinates(to)
                .setRadiusTo(ESTIMATE_CITY_RADIUS)
                .setEarliestDepartureDate(calculateEarliestDepartureTime().toLocalDate())
                .setLatestDepartureDate(calculateLatestDepartureTime().toLocalDate());
    }

    private LocalDateTime calculateEarliestDepartureTime() {
        return tripToDestination.getArrivalTime().plusHours(criteria.getHoursAtDestination());
    }

    private LocalDateTime calculateLatestDepartureTime() {
        return criteria.getLatestArrivalTime().minusSeconds(tripToDestination.getDurationSeconds());
    }
}
