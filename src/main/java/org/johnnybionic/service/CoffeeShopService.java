
package org.johnnybionic.service;

import org.johnnybionic.domain.CoffeeShop;

public interface CoffeeShopService {

	CoffeeShop findById(String coffeeShopId);
	CoffeeShop findByCoordinates(double longitude, double latitude);
	
}
