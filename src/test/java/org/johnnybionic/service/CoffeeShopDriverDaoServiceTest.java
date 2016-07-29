package org.johnnybionic.service;

import org.mockito.InjectMocks;

/**
 * The service doesn't do a great deal other than broker the DAO. It's there to
 * follow the controller/service/DAO pattern.
 * 
 *  A full database isn't required (that's done by the DAO implementations), so mocking
 *  can be used here. 
 * 
 * @author johnny
 *
 */

public class CoffeeShopDriverDaoServiceTest extends AbstractCoffeeShopServiceTest {

	@InjectMocks 
	CoffeeShopDriverDaoService service;

	@Override
	public CoffeeShopService getService() {
		return service;
	}
}
