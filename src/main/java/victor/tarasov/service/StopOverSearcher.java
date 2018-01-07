package victor.tarasov.service;

import victor.tarasov.model.trip.details.TripDetailsRequest;
import victor.tarasov.model.trip.details.response.Stop;
import victor.tarasov.model.trip.list.TripListRequest;
import victor.tarasov.model.trip.list.response.Trip;
import victor.tarasov.model.trip.list.response.TripId;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static victor.tarasov.model.trip.Locale.CROATIA;

public class StopOverSearcher {
    private Trip trip;
    private BlaBlaCarApi blaBlaCarApi = new BlaBlaCarApi();

    public StopOverSearcher(Trip trip) {
        this.trip = trip;
    }

    public List<Trip> findAllTrips() {
        TripDetailsRequest request = new TripDetailsRequest(trip.getTripId(), CROATIA);
        List<Trip> trips = visitedStops(blaBlaCarApi.getTripDetails(request).getStopOvers()).stream()
                .map(this::tripToStop)
                .collect(Collectors.toList());
        return trips;
    }

    private Trip tripToStop(Stop stop) {
        return blaBlaCarApi.getTripList(makeRequest(stop)).getTrips().stream()
                .filter(t -> trip.getTripId().getId().equals(t.getTripId().getId()))
                .findAny()
                .get();
    }

    private TripListRequest makeRequest(Stop stopOver) {
        return new TripListRequest().setRadiusFrom(1)
                .setRadiusTo(1)
                .setDepartureCoordinates(trip.getDeparturePlace().getCoordinates())
                .setArrivalCoordinates(stopOver.getCoordinates())
                .setEarliestDepartureDate(trip.getDepartureDate().toLocalDate())
                .setEarliestDepartureHour(trip.getDepartureDate().getHour())
                .setLatestDepartureHour(trip.getDepartureDate().getHour() + 1);
    }

    private List<Stop> visitedStops(List<Stop> stopOvers) {
        Stop departurePlace = null;
        for (Stop stopOver : stopOvers) {
            if (stopOver.isDeparturePlace()) departurePlace = stopOver;
        }
        Stop arrivalPlace = stopOvers.stream().filter(Stop::isArrivalPlace).findFirst().get();
        return stopOvers.subList(stopOvers.indexOf(departurePlace) + 1, stopOvers.indexOf(arrivalPlace));
    }
}
