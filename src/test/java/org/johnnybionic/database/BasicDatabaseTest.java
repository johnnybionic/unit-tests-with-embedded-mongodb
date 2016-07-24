package org.johnnybionic.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.johnnybionic.BasicSpringEmbeddedMongoApplication;
import org.johnnybionic.domain.CoffeeShop;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests that the embedded MongoDB server starts and can perform simple read/write operations,
 * and load data from test files. In other words, ensures 'proper' unit tests will work.
 * 
 * @author johnny
 *
 */

public class BasicDatabaseTest extends AbstractDatabaseTest {

	/*
	 * 1:
	 * Get a basic JSON file reader working.
	 */
	@Test
	public void thatJSONFileIsRead() throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		FileReader in = new FileReader(new File("src/test/resources/coffee-shops.json"));
		Object parse = jsonParser.parse(in);
		if (parse instanceof JSONArray) {
			JSONArray array = (JSONArray) parse;
			int counter = 0;
			for (Object o : array) {
				counter++;
			}
			assertTrue(counter >= 3);
		}
		else {
			fail();
		}
	}
	
	/* 2:
	 * Push the file reading bit into an abstract superclass.
	 * Also, the annotations used to run the test class.
	 */
	@Test
	public void thatJSONFileReaderIsShared() {
		JSONArray sampleData = readJSONFile("src/test/resources/coffee-shops.json");
		assertTrue(sampleData.size() >= 3);
	}
	
	/*
	 * 3: ensure sample data is in the database. There should be at least three records.
	 */
	@Test
	public void thatSampleDataIsInTheEmbeddedDatabase () {
		// when the test is run for the very first time, let's make sure this is actually wired up...
		assertNotNull(mongoTemplate);
		
		loadSampleData("src/test/resources/coffee-shops.json", COFFEESHOPS_COLLECTION);
		long count = getCollection(COFFEESHOPS_COLLECTION).count();
		assertTrue(count >= 3);
		
		removeCollection(COFFEESHOPS_COLLECTION);
		count = getCollection(COFFEESHOPS_COLLECTION).count();
		assertEquals(0, count);
	}

	/**
	 * We should be able to get actual instances from the template.
	 */
	@Test
	public void thatDomainClassesCanBeCreated() {
		loadSampleData("src/test/resources/coffee-shops.json", COFFEESHOPS_COLLECTION);
		List<CoffeeShop> findAll = mongoTemplate.findAll(CoffeeShop.class, COFFEESHOPS_COLLECTION);
		assertNotNull(findAll);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("openStreetMapId").is("N3593558608"));
		CoffeeShop findOne = mongoTemplate.findOne(query, CoffeeShop.class, COFFEESHOPS_COLLECTION);
		assertNotNull(findOne);
		assertEquals("Catherine's", findOne.getName());
		
		CoffeeShop findById = mongoTemplate.findById("57907ce15f55e14ae0b90ac9", CoffeeShop.class, COFFEESHOPS_COLLECTION);
		assertNotNull(findById);
		assertEquals(findOne, findById);

		removeCollection(COFFEESHOPS_COLLECTION);

	}
}
