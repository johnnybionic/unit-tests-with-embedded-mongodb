package org.johnnybionic.dao;

import java.util.Iterator;

import org.johnnybionic.domain.CoffeeShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * An implementation of the DAO that uses Spring's MongoTemplate.
 * 
 * @author johnny
 *
 */
@Component(value = "mongotemplate")
@Slf4j
public class CoffeeShopTemplateDao implements CoffeeShopDao {

    private static final String COFFEESHOPS_COLLECTION = "coffeeshops";
    private final MongoTemplate mongoTemplate;

    private final Double maxDistance = 2000.0;
    private final Double minDistance = 0.0;

    @Autowired
    public CoffeeShopTemplateDao(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CoffeeShop findById(final String coffeeShopId) {
        log.info("findById [{}]", coffeeShopId);
        final CoffeeShop findById = mongoTemplate.findById(coffeeShopId, CoffeeShop.class, COFFEESHOPS_COLLECTION);
        if (findById == null) {
            log.warn("Nothing found");
        }
        return findById;
    }

    @Override
    public CoffeeShop findByCoordinates(final double longitude, final double latitude) {
        log.info("findByCoordinates longitude [{}] latitude [{}]", longitude, latitude);

        final Point point = new Point(longitude, latitude);
        final Distance max = new Distance(maxDistance / 100, Metrics.KILOMETERS);
        final Distance min = new Distance(minDistance / 100, Metrics.KILOMETERS);

        // it's a train! :)
        final NearQuery near = NearQuery.near(point).spherical(true).maxDistance(max).minDistance(min)
                .query(new Query().limit(1));

        // it's interesting that we don't need to specify the 'location' field -
        // it must determine the index automatically
        final GeoResults<CoffeeShop> geoNear = mongoTemplate.geoNear(near, CoffeeShop.class, COFFEESHOPS_COLLECTION);

        final Iterator<GeoResult<CoffeeShop>> iterator = geoNear.iterator();
        if (iterator.hasNext()) {
            final GeoResult<CoffeeShop> next = iterator.next();
            return next.getContent();
        }

        log.warn("Nothing found");
        return null;
    }

}
