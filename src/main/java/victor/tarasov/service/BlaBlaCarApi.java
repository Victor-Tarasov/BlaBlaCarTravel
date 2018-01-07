package victor.tarasov.service;

import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import victor.tarasov.model.trip.details.TripDetailsRequest;
import victor.tarasov.model.trip.details.response.TripDetails;
import victor.tarasov.model.trip.list.TripListRequest;
import victor.tarasov.model.trip.list.response.TripListResponse;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.List;
import java.util.Map;

public class BlaBlaCarApi {
    private static final List<String> SECRET_KEYS = ImmutableList.of(
            "8eb3d43a72b3455ba8cf5971248caf5c",
            "5cb67d46560648d6a4d136463f235442",
            "3842499617c34c85804afbd018bbd3a4",
            "bafb0d239f25433aa402e4037951d7ee",
            "cfb39e92d3af4b658dd5b46d38a27c8b",
            "b4674583db57404fbcffc969e6fe138f",
            "a2926a0c44334358894666c83d42b120",
            "43adec8139ff4a36ac789ff0e975716e",
            "60adb73159aa4bcbab3086d570b570f3",
            "db2f8a7d883d4c7690ff4830e6a0d5f8",
            "256d4985f0e84027b652b07d84cbd60c",
            "ed44ff0444ed494d889f203856ae3eb9",
            "131c79bd2dfc4d77907df8d67e321b42",
            "a5d3c9b2c66244bcb2b9469189989e96",
            "d4509b26ffa040409d9755ee68eba982",
            "da5e3e3d98e04bf88660adf5f5bd02f8"
    );
    private static int secretKeysIndex = 0;
    private static final String SECRET_KEY_HEADER_NAME = "key";
    private static final String TRIPS_ENDPOINT_URL = "https://public-api.blablacar.com/api/v2/trips";
    private static int requestCount = 0;

    public TripListResponse getTripList(TripListRequest request) {
        WebTarget target = ClientBuilder.newBuilder().build().target(TRIPS_ENDPOINT_URL);
        for (Map.Entry<String, String> param : request.toParamsMap().entrySet()) {
            target = target.queryParam(param.getKey(), param.getValue());
        }

        return request(target, TripListResponse.class);
    }

    public TripDetails getTripDetails(TripDetailsRequest request) {
        WebTarget target = ClientBuilder.newBuilder().build()
                .target(TRIPS_ENDPOINT_URL)
                .path(request.getTripId().toString())
                .queryParam("locale", request.getLocale());
        return request(target, TripDetails.class);
    }

    @SneakyThrows
    private <T> T request(WebTarget target, Class<T> responseType) {
        try {
            T response = target.request()
                    .header(SECRET_KEY_HEADER_NAME, SECRET_KEYS.get(secretKeysIndex))
                    .get(responseType);
            requestCount++;
            if (requestCount % 100 == 0) {
                System.out.print("Request count: " + requestCount);
            }
            return response;
        } catch (ClientErrorException e) {
            String errorResponse = e.getResponse().readEntity(String.class);
            System.err.println(errorResponse);
            e.printStackTrace();
            return retry(target, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return retry(target, responseType);
        }
    }

    private <T> T retry(WebTarget target, Class<T> responseType) throws InterruptedException {
        if (++secretKeysIndex == SECRET_KEYS.size()) {
            secretKeysIndex = 0;
        }
        System.out.println("New secret key index: " + secretKeysIndex);
        return request(target, responseType);
    }
}
