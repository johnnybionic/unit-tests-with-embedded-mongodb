package org.johnnybionic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@SuppressWarnings("serial")
public class CoffeeShopNotFoundException extends RuntimeException {

	public CoffeeShopNotFoundException(String message) {
		super(message);
	}
	
}
