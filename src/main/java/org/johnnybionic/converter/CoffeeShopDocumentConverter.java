package org.johnnybionic.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.johnnybionic.domain.CoffeeShop;
import org.johnnybionic.domain.Coordinates;
import org.springframework.core.convert.converter.Converter;

/**
 * This version of the converter uses the newer Document class from the latest
 * MongoDB driver. When Spring Boot is updated, this converter can be used with
 * MongoTemplate. Until then it's used directly in the DAO.
 * 
 * @author johnny
 *
 */
public class CoffeeShopDocumentConverter implements Converter<Document, CoffeeShop> {

    static final String LOCATION_KEY = "location";
    static final String NAME_KEY = "name";
    static final String ID_KEY = "_id";
    static final String COORDINATES_KEY = "coordinates";

    @Override
    public CoffeeShop convert(final Document source) {
        final String name = source.getString(NAME_KEY);
        final String id = source.getObjectId(ID_KEY).toString();

        // the main reason for a custom converter is to convert the coordinates
        final Coordinates coordinates = getCoordinates(source);
        if (coordinates == null) {
            throw new RuntimeException(
                    String.format("Could not create coordinates from database entry with ID [%s] name [%s]", id, name));
        }

        // all attributes
        final Map<String, String> attributes = new HashMap<>();
        source.forEach((key, value) -> {
            attributes.put(key, value != null ? value.toString() : "null");
        });

        return new CoffeeShop(id, name, coordinates, attributes);
    }

    /**
     * Get the coordinates from the source.
     * 
     * @param source source
     * @return instance of {@link Coordinates}
     */
    private Coordinates getCoordinates(final Document source) {
        final Object location = source.get(LOCATION_KEY);
        if (location instanceof Document) {
            final Document document = (Document) location;
            final Object object = document.get(COORDINATES_KEY);
            if (object instanceof List) {
                final List<Double> list = (List) object;
                final Double longitude = list.get(0);
                final Double latitude = list.get(1);
                return new Coordinates(longitude, latitude);

            }
        }
        return null;
    }

}
