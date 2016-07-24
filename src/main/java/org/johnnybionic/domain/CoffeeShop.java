package org.johnnybionic.domain;

import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents the common data from each entry in the 'coffeeshops' collections.
 * 
 * @author johnny
 *
 */
@Data
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@EqualsAndHashCode
public class CoffeeShop {

	String _id;
	//String openStreetMapId;
	String name;
	//Object location;
	Coordinates coordinates;
	
	Map<String, String> attributes;
}
