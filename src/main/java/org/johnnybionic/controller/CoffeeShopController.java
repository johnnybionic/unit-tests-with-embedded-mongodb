package org.johnnybionic.controller;

import org.johnnybionic.domain.CoffeeShop;
import org.johnnybionic.exception.CoffeeShopNotFoundException;
import org.johnnybionic.service.CoffeeShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/service/coffeeshop")
@Slf4j
public class CoffeeShopController {
	
	@Autowired
	private CoffeeShopService service;

	@RequestMapping("/id/{id}")
	public CoffeeShop findById(@PathVariable(value = "id") String coffeeShopId) {
		log.debug("findById: [{}]", coffeeShopId);
		CoffeeShop findById = service.findById(coffeeShopId);
		if (findById == null) {
			String message = "Not found for id: " + coffeeShopId;
			log.error(message);
			throw new CoffeeShopNotFoundException(message);
		}
		
		return findById;
	}

    /*
    * Pattern required on last parameter to prevent Spring treating it as an extension e.g. .xml, .json
     */
	@RequestMapping("/nearest/{latitude}/{longitude:.*}")
	public CoffeeShop findByCoordinates(@PathVariable(value = "latitude") Double latitude, @PathVariable(value = "longitude") Double longitude) {
		log.debug("findByCoordinates latitude [{}] longitude [{}]", latitude, longitude);
		CoffeeShop findByCoordinates = service.findByCoordinates(longitude, latitude);
		if (findByCoordinates == null) {
			String message = String.format("Not found for [%f][%f]", latitude, longitude);
			log.error(message);
			throw new CoffeeShopNotFoundException(message);
		}
		
		return findByCoordinates;
	}

}
