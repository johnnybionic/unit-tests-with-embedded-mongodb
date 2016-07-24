package org.johnnybionic.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MongoDB has a Point class but it's rather specific. Java AWT has coordinates but we're not using that ...
 * 
 * @author johnny
 *
 */
@Data
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class Coordinates {

	private Double longitude;
	private Double latitude;
}
