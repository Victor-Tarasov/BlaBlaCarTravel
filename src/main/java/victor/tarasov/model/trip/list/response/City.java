package victor.tarasov.model.trip.list.response;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Locale;
import java.util.Map;

import static java.util.Locale.ENGLISH;
import static lombok.AccessLevel.PRIVATE;

@Getter
public class City {
    private static final Map<CountryCityKey, City> cities = Maps.newHashMap();
    private static final Multiset<CountryCityKey> occurrenceCount = HashMultiset.create();

    private String cityName;
    private String country;
    private Coordinates coordinates;

    private City(String cityName, String country, Float latitude, Float longitude) {
        this.cityName = cityName;
        this.country = country;
        this.coordinates = new Coordinates(latitude, longitude);
    }

    static City instance(String cityName, Float latitude, Float longitude, String countryCode) {
        String country = countryCodeToCountry(countryCode);
        CountryCityKey key = new CountryCityKey(country, cityName);
        City city = cities.get(key);
        if (city == null) {
            city = new City(cityName, country, latitude, longitude);
            cities.put(key, city);
        }

        int occurrencesCount = occurrenceCount.count(key);
        averageCoordinates(city.coordinates, occurrencesCount, latitude, longitude);
        occurrenceCount.add(key);
        return city;
    }

    private static void averageCoordinates(Coordinates coordinates, int occurrencesCount, Float latitude, Float longitude) {
        coordinates.setLatitude(countAverage(coordinates.getLatitude(), latitude, occurrencesCount));
        coordinates.setLongitude(countAverage(coordinates.getLongitude(), longitude, occurrencesCount));
    }

    private static float countAverage(Float oldValue, Float newValue, int occurrencesCount) {
        return (oldValue * occurrencesCount + newValue) / (occurrencesCount + 1);
    }

    private static String countryCodeToCountry(String countryCode) {
        return new Locale("", countryCode).getDisplayCountry(ENGLISH);
    }

    @EqualsAndHashCode
    @AllArgsConstructor(access = PRIVATE)
    private static class CountryCityKey {
        private String cityName;
        private String country;
    }
}
