package org.johnnybionic.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.johnnybionic.domain.CoffeeShop;
import org.johnnybionic.domain.Coordinates;
import org.springframework.core.convert.converter.Converter;

/**
 * This version of the converter uses the newer Document class from the latest MongoDB driver. When
 * Spring Boot is updated, this converter can be used with MongoTemplate. Until then it's used
 * directly in the DAO.
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
	public CoffeeShop convert(Document source) {
		String name = source.getString(NAME_KEY);
		String _id = source.getObjectId(ID_KEY).toString();

		// the main reason for a custom converter is to convert the coordinates
		Coordinates coordinates = getCoordinates(source);
		if (coordinates == null){
			throw new RuntimeException(String.format("Could not create coordinates from database entry with ID [%s] name [%s]", _id, name));
		}

		// all attributes
		Map<String, String> attributes = new HashMap<>();
		source.forEach((key, value) -> {
			attributes.put(key, value != null ? value.toString() : "null");
		});
		
		return new CoffeeShop(_id, name, coordinates, attributes);
	}

	private Coordinates getCoordinates(Document source) {
		Object location = source.get(LOCATION_KEY);
		if (location instanceof Document) {
			Document document = (Document) location;
			Object object = document.get(COORDINATES_KEY);
			if (object instanceof List) {
				List<Double> list = (List) object;
				Double longitude = (Double) list.get(0);
				Double latitude = (Double) list.get(1);
				return new Coordinates(longitude, latitude);
				
				
			}
			int x = 1;
		}
		return null;
	}

}
