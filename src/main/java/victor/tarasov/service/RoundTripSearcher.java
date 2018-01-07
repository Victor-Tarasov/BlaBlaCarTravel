package victor.tarasov.service;

import victor.tarasov.model.Criteria;
import victor.tarasov.model.trip.RoundTrip;
import victor.tarasov.model.trip.list.response.Coordinates;
import victor.tarasov.model.trip.list.response.Trip;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RoundTripSearcher {
    private BalkOperationExecutor balkOperationExecutor = new BalkOperationExecutor();
    private Criteria criteria;
    private Coordinates from;

    public RoundTripSearcher(Coordinates from, Criteria criteria) {
        this.criteria = criteria;
        this.from = from;
    }

    public List<RoundTrip> findAllTrips() {
        List<Trip> forwardTrips = new ForwardTripSearcher(from, criteria).findAllTrips();
        System.out.println("Find round trips");
        return balkOperationExecutor.runBalkOperation(forwardTrips, this::findRoundTrips)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<RoundTrip> findRoundTrips(Trip forwardTrip) {
        return findAllReturnTrips(forwardTrip).stream()
                .map(returnTrip -> new RoundTrip(forwardTrip, returnTrip))
                .collect(Collectors.toList());
    }

    private List<Trip> findAllReturnTrips(Trip trip) {
        return new ReturnTripSearcher(trip, from, criteria).findAllTrips();
    }
}
