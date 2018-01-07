package victor.tarasov.service;

import com.google.common.collect.Lists;
import victor.tarasov.model.Criteria;
import victor.tarasov.model.trip.RoundTrip;
import victor.tarasov.model.trip.list.response.Coordinates;
import victor.tarasov.model.trip.list.response.Trip;

import java.util.List;

public class RoundTripSearcher {
    private Criteria criteria;
    private Coordinates from;

    public RoundTripSearcher(Coordinates from, Criteria criteria) {
        this.criteria = criteria;
        this.from = from;
    }

    public List<RoundTrip> findAllTrips() {
        List<RoundTrip> roundTrips = Lists.newArrayList();
        List<Trip> forwardTrips = new TripFromSearcher(from, criteria).findAllTrips();
        for (int i = 0; i < forwardTrips.size(); i++) {
            Trip forwardTrip = forwardTrips.get(i);
            for (Trip returnTrip : findAllReturnTrips(forwardTrip)) {
                roundTrips.add(new RoundTrip(forwardTrip, returnTrip));
            }
            System.out.printf("\rFind round trips %d/%d trips." , i, forwardTrips.size() - 1);
        }
        return roundTrips;
    }

    private List<Trip> findAllReturnTrips(Trip trip) {
        return new ReturnTripSearcher(trip, from, criteria).findAllTrips();
    }
}
