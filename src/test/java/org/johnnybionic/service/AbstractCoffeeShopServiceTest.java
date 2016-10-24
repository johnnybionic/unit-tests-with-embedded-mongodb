package org.johnnybionic.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.johnnybionic.dao.CoffeeShopDao;
import org.johnnybionic.domain.CoffeeShop;
import org.johnnybionic.domain.Coordinates;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Both versions of the service need to pass the same tests. This saves cutting
 * and pasting the tests between the two, plus avoids missing tests, or any
 * workarounds that should be fixed in the implementation.
 * 
 * There are arguments against using inheritance for tests, and I've worked in a
 * project that had about six levels of inheritance and was a nightmare, but I
 * think I can get away with it here. There shouldn't be two services anyway,
 * it's only to get round a problem with profiles.
 * 
 * @author johnny
 *
 */
public abstract class AbstractCoffeeShopServiceTest {

    public static final String COFFEE_SHOP_ID = "57907ce15f55e14ae0b90ac9";
    public static final String NAME = "shop";
    public static final Double LATITUDE = 52.1d;
    public static final Double LONGITUDE = -0.1d;

    @Mock
    private CoffeeShopDao dao;

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void thatFindByIdWorks() {
        when(dao.findById(COFFEE_SHOP_ID)).thenReturn(getCoffeeShop());
        final CoffeeShop shop = getService().findById(COFFEE_SHOP_ID);
        assertNotNull(shop);
        assertEquals(COFFEE_SHOP_ID, shop.getId());
        verify(dao).findById(COFFEE_SHOP_ID);
    }

    @Test
    public void thatFindByLocationReturnsAShop() {
        when(dao.findByCoordinates(LONGITUDE, LATITUDE)).thenReturn(getCoffeeShop());
        final CoffeeShop shop = getService().findByCoordinates(LONGITUDE, LATITUDE);
        verify(dao).findByCoordinates(LONGITUDE, LATITUDE);

        assertNotNull(shop);
        assertEquals(COFFEE_SHOP_ID, shop.getId());

    }

    public static CoffeeShop getCoffeeShop() {
        final Map<String, String> attributes = new HashMap<>();
        final Coordinates coordinates = new Coordinates(LONGITUDE, LATITUDE);
        return new CoffeeShop(COFFEE_SHOP_ID, NAME, coordinates, attributes);
    }

    public abstract CoffeeShopService getService();
}
