package org.johnnybionic.dao;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.johnnybionic.converter.CoffeeShopDocumentConverter;
import org.johnnybionic.domain.CoffeeShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component(value = "mongodriver")
@Slf4j
public class CoffeeShopDriverDao implements CoffeeShopDao {

    private static final String COFFEESHOPS_COLLECTION = "coffeeshops";
    private static final String LOCATION_FIELD = "location";

    private final MongoCollection<Document> collection;
    private final CoffeeShopDocumentConverter converter;

    private final Double maxDistance = 2000.0;
    private final Double minDistance = 0.0;

    @Autowired
    public CoffeeShopDriverDao(final MongoDatabase database) {
        collection = database.getCollection(COFFEESHOPS_COLLECTION);
        converter = new CoffeeShopDocumentConverter();
    }

    @Override
    public CoffeeShop findById(final String coffeeShopId) {
        log.info("findById [{}]", coffeeShopId);

        final ObjectId objectId = new ObjectId(coffeeShopId);
        final Bson filter = Filters.eq("_id", objectId);
        final ArrayList<Document> results = collection.find(filter).into(new ArrayList<Document>());
        if (results != null && results.size() == 1) {
            final Document document = results.get(0);
            document.get("name");
            return converter.convert(document);
        }

        log.warn("Nothing found");
        return null;
    }

    @Override
    public CoffeeShop findByCoordinates(final double longitude, final double latitude) {
        log.info("findByCoordinates longitude [{}] latitude [{}]", longitude, latitude);

        final Point point = new Point(new Position(longitude, latitude));
        final Bson filter = Filters.near(LOCATION_FIELD, point, maxDistance, minDistance);
        final ArrayList<Document> results = collection.find(filter).into(new ArrayList<Document>());
        if (results != null && results.size() > 0) {
            return converter.convert(results.get(0));
        }

        log.warn("Nothing found");
        return null;
    }

}
