package org.johnnybionic.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Boot does not provide MongoDatabase - it provides the older MongoDB 
 * - hence this configuration.
 * 
 * @author johnny
 *
 */
@Configuration("!junit")
@Slf4j
public class MongoDatabaseConfiguration {

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Value("${spring.data.mongodb.database}")
	private String database;

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.uri:#{null}}")
	private String uri;

	@Bean
	public MongoDatabase mongoDatabase() {
		
        MongoClient client = (uri == null || uri.length() == 0) ? 
        		new MongoClient(host, port) : new MongoClient(new MongoClientURI(uri));
        log.info("Using Mongo instance: " + client.getAddress().toString());
        MongoDatabase mongoDatabase = client.getDatabase(database);
        return mongoDatabase;
		
	}

}
