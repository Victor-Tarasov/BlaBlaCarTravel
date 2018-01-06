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
     */
    private String cityFrom;

    /**
     * In Czech.
     */
    private String cityTo;

    @Override
    public String toString() {
        return id + "-" + cityFrom + "-" + cityTo;
    }

    public TripId copy() {
        return new TripId(id, cityFrom, cityTo);
    }
}
