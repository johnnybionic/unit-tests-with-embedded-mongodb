package org.johnnybionic.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Spring Boot does not provide MongoDatabase - it provides the older MongoDB - hence this configuration.
 * 
 * @author johnny
 *
 */
@Profile("junit")
@Configuration
public class UnitTestCoffeeShopConfiguration {

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Value("${spring.data.mongodb.database}")
	private String database;

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Bean
	public MongoDatabase mongoDatabase() {

        MongoClient client = new MongoClient(host, port);
        MongoDatabase mongoDatabase = client.getDatabase(database);
        return mongoDatabase;
		
	}
}
