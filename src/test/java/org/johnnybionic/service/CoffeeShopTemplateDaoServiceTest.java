package org.johnnybionic.service;

import org.mockito.InjectMocks;

public class CoffeeShopServiceTemplateDaoTest extends AbstractCoffeeShopServiceTest {

	@InjectMocks
	private CoffeeShopServiceTemplateDao service;

	@Override
	public CoffeeShopService getService() {
		return service;
	}
	
}
