package org.johnnybionic.controller;

import static org.johnnybionic.service.AbstractCoffeeShopServiceTest.COFFEE_SHOP_ID;
import static org.johnnybionic.service.AbstractCoffeeShopServiceTest.LATITUDE;
import static org.johnnybionic.service.AbstractCoffeeShopServiceTest.LONGITUDE;
import static org.johnnybionic.service.AbstractCoffeeShopServiceTest.getCoffeeShop;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.johnnybionic.domain.CoffeeShop;
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
		CoffeeShop findById = controller.findById(COFFEE_SHOP_ID);
		verify(service).findById(COFFEE_SHOP_ID);
		assertNotNull(findById);
		assertEquals(COFFEE_SHOP_ID, findById.get_id());
	}

	@Test
	public void thatFindByLocationReturnsShop() {
		when(service.findByCoordinates(LONGITUDE, LATITUDE)).thenReturn(getCoffeeShop());
		controller.findByCoordinates(LONGITUDE, LATITUDE);
		verify(service).findByCoordinates(LONGITUDE, LATITUDE);
	}

}
