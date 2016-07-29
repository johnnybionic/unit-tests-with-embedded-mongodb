package org.johnnybionic.converter;

import java.util.HashMap;
import java.util.Map;

import org.johnnybionic.domain.CoffeeShop;
import org.johnnybionic.domain.Coordinates;
import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

/**
 * This version of the converter uses the older BasicDBObject class, from the MongoDB 2.x driver. 
 * Spring Boot uses this version, and this converter is registered to perform conversions when reading
 * from the database using MongoTemplate.
 * 
 * @author johnny
 *
 */
public class CoffeeShopConverter implements Converter<BasicDBObject, CoffeeShop> {

	static final String LOCATION_KEY = "location";
	static final String NAME_KEY = "name";
	static final String ID_KEY = "_id";
	static final String COORDINATES_KEY = "coordinates";

	@Override
	public CoffeeShop convert(BasicDBObject source) {
		String name = source.getString(NAME_KEY);
		String _id = source.getString(ID_KEY);

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

	private Coordinates getCoordinates(BasicDBObject source) {
		Object location = source.get(LOCATION_KEY);
		if (location instanceof BasicDBObject) {
			BasicDBObject map = (BasicDBObject) location;
			Object object = map.get(COORDINATES_KEY);
			if (object instanceof BasicDBList) {
				BasicDBList list = (BasicDBList) object;

				Double longitude = (Double) list.get(0);
				Double latitude = (Double) list.get(1);
				return new Coordinates(longitude, latitude);
			}
		}
		
		return null;
	}
}
