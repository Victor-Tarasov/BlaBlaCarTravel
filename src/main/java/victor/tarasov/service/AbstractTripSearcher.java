package victor.tarasov.service;

import com.google.common.collect.Lists;
import victor.tarasov.model.Criteria;
import victor.tarasov.model.trip.list.TripListRequest;
import victor.tarasov.model.trip.list.response.Pager;
import victor.tarasov.model.trip.list.response.Trip;
import victor.tarasov.model.trip.list.response.TripListResponse;

import java.util.List;

abstract public class AbstractTripSearcher {
    protected BlaBlaCarApi blaBlaCarApi = new BlaBlaCarApi();
    protected Criteria criteria;

    public AbstractTripSearcher(Criteria criteria) {
        this.criteria = criteria;
    }

    protected List<Trip> extractTripsFromAllPages(TripListRequest request) {
        List<Trip> trips = Lists.newArrayList();
        Pager pager;
        int page = 0;
        do {
            TripListResponse tripListResponse = blaBlaCarApi.getTripList(request.setPage(page + 1));
            trips.addAll(tripListResponse.getTrips());
            pager = tripListResponse.getPager();
            page = pager.getPage();
        } while (page < pager.getPages());
        return trips;
    }

    abstract public List<Trip> findAllTrips();
}
