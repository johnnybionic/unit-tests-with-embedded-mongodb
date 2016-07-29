package org.johnnybionic.dao;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.johnnybionic.converter.CoffeeShopDocumentConverter;
import org.johnnybionic.domain.CoffeeShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import lombok.extern.slf4j.Slf4j;

/**
 * An implementation of the DAO that uses the MongoDB driver directly.
 * 
 * @author johnny
 *
 */
@Component(value="mongodriver")
//@Profile("mongodriver")
@Slf4j
public class CoffeeShopDriverDao implements CoffeeShopDao {
	
	private static final String COFFEESHOPS_COLLECTION = "coffeeshops";
	private static final String LOCATION_FIELD = "location";
	
	private MongoCollection<Document> collection;
	private CoffeeShopDocumentConverter converter;

	private Double maxDistance = 2000.0;
	private Double minDistance = 0.0;

	@Autowired
	public CoffeeShopDriverDao(MongoDatabase database) {
		collection = database.getCollection(COFFEESHOPS_COLLECTION);
		converter = new CoffeeShopDocumentConverter();
	}
	
	@Override
	public CoffeeShop findById(String coffeeShopId) {
		log.info("findById [{}]", coffeeShopId);
		
		ObjectId objectId = new ObjectId(coffeeShopId);
		Bson filter = Filters.eq("_id", objectId);
		ArrayList<Document> results = collection.find(filter).into(new ArrayList<Document>());
		if (results != null && results.size() == 1) {
			Document document = results.get(0);
			document.get("name");
			return converter.convert(document);
		}
		
		log.warn("Nothing found");
		return null;
	}

	@Override
	public CoffeeShop findByCoordinates(double longitude, double latitude) {
		log.info("findByCoordinates longitude [{}] latitude [{}]", longitude, latitude);
		
		Point point = new Point(new Position(longitude, latitude));
		Bson filter = Filters.near(LOCATION_FIELD, point, maxDistance, minDistance);
		ArrayList<Document> results = collection.find(filter).into(new ArrayList<Document>());
		if (results != null && results.size() > 0) {
			return converter.convert(results.get(0));
		}
		
		log.warn("Nothing found");
		return null;
	}

}
