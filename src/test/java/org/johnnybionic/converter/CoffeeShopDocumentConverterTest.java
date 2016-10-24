package org.johnnybionic.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.johnnybionic.domain.CoffeeShop;
import org.junit.Before;
import org.junit.Test;

public class CoffeeShopDocumentConverterTest {

    private static final String TEST_ID = "57907ce15f55e14ae0b90ac9";
    private static final String TEST_NAME = "shop";
    private static final Double LONGITUDE = -0.1D;
    private static final Double LATITUDE = 52.13D;
    private static final String ATTRIBUTE_1 = "attr1";
    private static final String ATTRIBUTE_2 = "attr2";

    private CoffeeShopDocumentConverter converter;

    @Before
    public void setUp() {
        converter = new CoffeeShopDocumentConverter();
    }

    @Test
    public void thatCoffeeShopIsCreated() {
        final Document source = getSource();

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

        final Document source = getSource().append(ATTRIBUTE_1, ATTRIBUTE_1).append(ATTRIBUTE_2, ATTRIBUTE_2);
        final CoffeeShop result = converter.convert(source);
        assertNotNull(result);
        assertNotNull(result.getAttributes());
        assertEquals(5, result.getAttributes().size());

    }

    @Test(expected = RuntimeException.class)
    public void thatExceptionOccursForMissingCoordinates() {
        final Document source = new Document(CoffeeShopConverter.ID_KEY, TEST_ID);
        converter.convert(source);
    }

    private Document getSource() {
        final ObjectId id = new ObjectId(TEST_ID);
        final Document source = new Document(CoffeeShopConverter.ID_KEY, id);
        source.put(CoffeeShopConverter.NAME_KEY, TEST_NAME);

        final List<Double> coordinates = new ArrayList<>();
        coordinates.add(LONGITUDE);
        coordinates.add(LATITUDE);
        final Document location = new Document(CoffeeShopConverter.COORDINATES_KEY, coordinates);
        source.put(CoffeeShopConverter.LOCATION_KEY, location);
        return source;
    }

}
