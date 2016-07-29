package org.johnnybionic.service;

import org.mockito.InjectMocks;

public class CoffeeShopTemplateDaoServiceTest extends AbstractCoffeeShopServiceTest {

	@InjectMocks
	private CoffeeShopTemplateDaoService service;

	@Override
	public CoffeeShopService getService() {
		return service;
	}
	
}
