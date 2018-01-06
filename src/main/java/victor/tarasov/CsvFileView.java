package victor.tarasov;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import victor.tarasov.model.trip.RoundTrip;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CsvFileView {

    @SneakyThrows
    public void output(List<RoundTrip> roundTrips) {
        String tripsCsv = roundTrips.stream()
                .map(RoundTrip::toCsv)
                .collect(Collectors.joining("\r\n"));
        String fileContent = RoundTrip.header() + "\r\n" + tripsCsv;
        File file = new File(makeFileName());
        file.createNewFile();
        FileUtils.writeStringToFile(file, fileContent);
    }

    private String makeFileName() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        return datePrefix + "-" + "round_trips.csv";
    }
}
