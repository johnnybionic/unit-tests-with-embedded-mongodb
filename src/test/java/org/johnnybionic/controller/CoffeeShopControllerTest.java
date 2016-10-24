package org.johnnybionic.controller;

import static org.johnnybionic.service.AbstractCoffeeShopServiceTest.COFFEE_SHOP_ID;
import static org.johnnybionic.service.AbstractCoffeeShopServiceTest.LATITUDE;
import static org.johnnybionic.service.AbstractCoffeeShopServiceTest.LONGITUDE;
import static org.johnnybionic.service.AbstractCoffeeShopServiceTest.getCoffeeShop;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.johnnybionic.domain.CoffeeShop;
import org.johnnybionic.exception.CoffeeShopNotFoundException;
import org.johnnybionic.service.CoffeeShopService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CoffeeShopControllerTest {

    @InjectMocks
    private CoffeeShopController controller;

    @Mock
    private CoffeeShopService service;

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void thatFindByIdReturnsShop() {
        when(service.findById(COFFEE_SHOP_ID)).thenReturn(getCoffeeShop());
        final CoffeeShop findById = controller.findById(COFFEE_SHOP_ID);
        verify(service).findById(COFFEE_SHOP_ID);
        assertNotNull(findById);
        assertEquals(COFFEE_SHOP_ID, findById.getId());
    }

    @Test
    public void thatFindByLocationReturnsShop() {
        when(service.findByCoordinates(LONGITUDE, LATITUDE)).thenReturn(getCoffeeShop());
        controller.findByCoordinates(LATITUDE, LONGITUDE);
        verify(service).findByCoordinates(LONGITUDE, LATITUDE);
    }

    @Test(expected = CoffeeShopNotFoundException.class)
    public void thatExceptionThrownIfNoShopFoundById() {
        when(service.findById(anyString())).thenReturn(null);
        controller.findById(COFFEE_SHOP_ID);

    }

    @Test(expected = CoffeeShopNotFoundException.class)
    public void thatExceptionThrownIfNoShopFoundByLocation() {
        when(service.findByCoordinates(anyDouble(), anyDouble())).thenReturn(null);
        controller.findByCoordinates(0d, 0d);

    }

}
