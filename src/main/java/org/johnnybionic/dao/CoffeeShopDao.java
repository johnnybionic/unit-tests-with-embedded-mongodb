package org.johnnybionic.dao;

import org.johnnybionic.domain.CoffeeShop;

public interface CoffeeShopDao {

	/**
	 * Find by the document's object ID
	 * @param coffeeShopId the ID
	 * @return a {@link CoffeeShop}, or null if not found
	 */
	CoffeeShop findById(String coffeeShopId);
	
	/**
	 * Find the nearest Document to the given coordinates.
	 * 
	 * @param longitude the longitude
	 * @param latitude the latitude
	 * @return a {@link CoffeeShop}, or null if none found nearby
	 */
	CoffeeShop findByCoordinates(double longitude, double latitude);

}
