package victor.tarasov.service;

import lombok.SneakyThrows;
import victor.tarasov.model.trip.details.TripDetailsRequest;
import victor.tarasov.model.trip.details.response.TripDetails;
import victor.tarasov.model.trip.list.TripListRequest;
import victor.tarasov.model.trip.list.response.TripListResponse;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Map;

public class BlaBlaCarApi {
    public static final String SECRET_KEY = "5cb67d46560648d6a4d136463f235442";
    public static final String SECRET_KEY_HEADER_NAME = "key";
    private static final String TRIPS_ENDPOINT_URL = "https://public-api.blablacar.com/api/v2/trips";
    private static int requestCount;

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
        Thread.sleep(1000);
        try {
            T response = target.request()
                    .header(SECRET_KEY_HEADER_NAME, SECRET_KEY)
                    .get(responseType);
            if (requestCount % 100 == 0) {
                System.out.print("Request count: " + requestCount++);
            }
            return response;
        } catch (ClientErrorException e) {
            String errorResponse = ((ClientErrorException) e).getResponse().readEntity(String.class);
            System.err.println(errorResponse);
            e.printStackTrace();
            return retry(target, responseType);
        } catch (Exception e) {
            e.printStackTrace();
            return retry(target, responseType);
        }
    }

    private <T> T retry(WebTarget target, Class<T> responseType) throws InterruptedException {
        Thread.sleep(500);
        return request(target, responseType);
    }
}
