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

public abstract class AbstractCoffeeShopServiceTest {

	private static final String COFFEE_SHOP_ID = "57907ce15f55e14ae0b90ac9";
	private static final String NAME = "shop";
	private static final Double LATITUDE = 52.1d;
	private static final Double LONGITUDE = -0.1d;
	
	@Mock
	private CoffeeShopDao dao;

	@Before
	public void initTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void thatFindByIdWorks() {
		when(dao.findById(COFFEE_SHOP_ID)).thenReturn(getCoffeeShop());
		CoffeeShop shop = getService().findById(COFFEE_SHOP_ID);
		assertNotNull(shop);
		assertEquals(COFFEE_SHOP_ID, shop.get_id());
		verify(dao).findById(COFFEE_SHOP_ID);
	}

	private CoffeeShop getCoffeeShop() {
		Map<String, String> attributes = new HashMap<>();
		Coordinates coordinates = new Coordinates(LONGITUDE, LATITUDE);
		return new CoffeeShop(COFFEE_SHOP_ID, NAME, coordinates, attributes);
	}

	public abstract CoffeeShopService getService();
}
