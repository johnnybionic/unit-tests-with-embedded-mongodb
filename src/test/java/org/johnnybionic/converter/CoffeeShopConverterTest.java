package org.johnnybionic.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.johnnybionic.domain.CoffeeShop;
import org.johnnybionic.domain.Coordinates;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

/**
 * The converter is used to create {@link CoffeeShop} instances from documents
 * returned from the coffeeshops collection. The main reason it exists is to
 * convert the location into a {@link Coordinates} instance, as the default
 * converter(s) can't do that.
 * 
 * @author johnny
 *
 */
public class CoffeeShopConverterTest {

    private static final String TEST_ID = "42424242";
    private static final String TEST_NAME = "shop";
    private static final Double LONGITUDE = -0.1D;
    private static final Double LATITUDE = 52.13D;
    private static final String ATTRIBUTE_1 = "attr1";
    private static final String ATTRIBUTE_2 = "attr2";

    private CoffeeShopConverter converter;

    @Before
    public void setUp() {
        converter = new CoffeeShopConverter();
    }

    @Test
    public void thatCoffeeShopIsCreated() {
        final BasicDBObject source = getSource();

        final CoffeeShop result = converter.convert(source);
        assertNotNull(result);

        assertEquals(TEST_NAME, result.getName());
        assertEquals(TEST_ID, result.getId());
        assertNotNull(result.getCoordinates());
        assertEquals(LONGITUDE, result.getCoordinates().getLongitude());
        assertEquals(LATITUDE, result.getCoordinates().getLatitude());

        // there should be three attributes - the ones that are explicitly set
        // as members of the CoffeShop
        assertNotNull(result.getAttributes());
        assertEquals(3, result.getAttributes().size());
    }

    @Test
    public void thatExtraAttributesAreReturned() {

        final BasicDBObject source = getSource().append(ATTRIBUTE_1, ATTRIBUTE_1).append(ATTRIBUTE_2, ATTRIBUTE_2);
        final CoffeeShop result = converter.convert(source);
        assertNotNull(result);
        assertNotNull(result.getAttributes());
        assertEquals(5, result.getAttributes().size());

    }

    @Test(expected = RuntimeException.class)
    public void thatExceptionOccursForMissingCoordinates() {
        final BasicDBObject source = new BasicDBObject(CoffeeShopConverter.ID_KEY, TEST_ID);
        converter.convert(source);
    }

    private BasicDBObject getSource() {
        final BasicDBObject source = new BasicDBObject(CoffeeShopConverter.ID_KEY, TEST_ID);
        source.put(CoffeeShopConverter.NAME_KEY, TEST_NAME);

        final BasicDBList coordinates = new BasicDBList();
        coordinates.add(LONGITUDE);
        coordinates.add(LATITUDE);
        final BasicDBObject location = new BasicDBObject(CoffeeShopConverter.COORDINATES_KEY, coordinates);
        source.put(CoffeeShopConverter.LOCATION_KEY, location);
        return source;
    }

}
