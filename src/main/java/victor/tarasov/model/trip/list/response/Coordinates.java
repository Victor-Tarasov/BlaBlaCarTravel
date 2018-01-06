package victor.tarasov.model.trip.list.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static lombok.AccessLevel.PACKAGE;

@Getter
@Setter(PACKAGE)
@AllArgsConstructor
public class Coordinates {
    private Float latitude;
    private Float longitude;



    @Override
    public String toString() {
        return latitude + "|" + longitude;
    }
}
