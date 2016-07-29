package org.johnnybionic.controller;

import org.johnnybionic.domain.CoffeeShop;
import org.johnnybionic.service.CoffeeShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coffeeshop")
public class CoffeeShopController {
	
	@Autowired
	private CoffeeShopService service;

	@RequestMapping("/id/{id}")
	public CoffeeShop findById(@PathVariable(value = "id") String coffeeShopId) {
		return service.findById(coffeeShopId);
		
	}

    /*
    * Pattern required on last parameter to prevent Spring treating it as an extension e.g. .xml, .json
     */
	@RequestMapping("/location/{longitude}/{latitude:.*}")
	public CoffeeShop findByCoordinates(@PathVariable(value = "latitude") Double latitude, @PathVariable(value = "longitude") Double longitude) {
		return service.findByCoordinates(longitude, latitude);
	}

}
