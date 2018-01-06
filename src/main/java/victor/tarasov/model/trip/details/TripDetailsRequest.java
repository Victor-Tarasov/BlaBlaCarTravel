package victor.tarasov.model.trip.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import victor.tarasov.model.trip.Locale;
import victor.tarasov.model.trip.list.response.TripId;

@Getter
@Setter
@AllArgsConstructor
public class TripDetailsRequest {
    private TripId tripId;
    private Locale locale;
}
