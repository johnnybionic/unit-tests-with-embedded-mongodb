package org.johnnybionic;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.johnnybionic.converter.CoffeeShopConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

@SpringBootApplication
public class BasicSpringEmbeddedMongoApplication {

	@Autowired
	private MappingMongoConverter converter;
	
	public static void main(String[] args) {
		SpringApplication.run(BasicSpringEmbeddedMongoApplication.class, args);
		
	}
	
	/**
	 * Registers a custom converter for our domain class. Spring documentation is out of date for this :(
	 * So basically, work it out for yourself ...
	 * This seems as good a place as any to do this - if it's done as part of configuration (i.e. AbstractMongoConfiguration),
	 * it requires a lot more to be set, things which are already set by default. Instead, let's get the one Spring creates and add to it.
	 */
	@PostConstruct
	private void init() {
		
		List<CoffeeShopConverter> converters = new LinkedList<>();
		converters.add(new CoffeeShopConverter());
		CustomConversions conversions = new CustomConversions(converters);
		converter.setCustomConversions(conversions );
		// important - took ages to find this was required!
		converter.afterPropertiesSet();
		
	}
}
