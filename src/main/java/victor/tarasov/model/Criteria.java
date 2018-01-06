package victor.tarasov.model;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class Criteria {
    private LocalDateTime earliestDepartureTime;
    private LocalDateTime latestArrivalTime;
    private Integer hoursAtDestination;
    private Integer price;

    public Criteria(@NonNull LocalDateTime earliestDepartureTime,
                    @NonNull LocalDateTime latestArrivalTime,
                    @NonNull Integer hoursAtDestination,
                    @NonNull Integer price) {
        this.earliestDepartureTime = earliestDepartureTime;
        this.latestArrivalTime = latestArrivalTime;
        this.hoursAtDestination = hoursAtDestination;
        this.price = price;
    }
}
