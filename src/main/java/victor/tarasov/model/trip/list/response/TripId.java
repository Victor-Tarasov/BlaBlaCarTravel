package victor.tarasov.model.trip.list.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
public class TripId {
    private Integer id;

    /**
     * In Czech.
     * Name of departure and destination.
     */
    private String textPart;

    @Override
    public String toString() {
        return id + "-" + textPart;
    }

    public TripId copy() {
        return new TripId(id, textPart);
    }
}
