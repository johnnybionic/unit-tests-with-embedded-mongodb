package org.johnnybionic.database;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.johnnybionic.BasicSpringEmbeddedMongoApplication;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBCollection;

/**
 * A base class for database tests.
 * 
 * @author johnny
 *
 */

@ActiveProfiles({"junit"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BasicSpringEmbeddedMongoApplication.class)
public abstract class AbstractDatabaseTest {

	protected static final String COFFEESHOPS_COLLECTION = "coffeeshops";
	@Autowired
	protected MongoTemplate mongoTemplate;

	/**
	 * Reads a file of JSON objects and returns an array of the objects, or fails
	 * if there's any problems. 
	 * 
	 * @param fileName the file to read
	 * @return a JSONArray of the file's contents
	 */
	protected JSONArray readJSONFile(String fileName) {
	
		JSONParser jsonParser = new JSONParser();
		FileReader in;
		try {
			in = new FileReader(new File(fileName));
			Object parse = jsonParser.parse(in);
			if (parse instanceof JSONArray) {
				return (JSONArray) parse;
			}
			else {
				fail("Wrong type returned by parser  for " + fileName);
			}
		} 
		catch (ParseException | IOException e) {
			fail("Could not read " + fileName);
		}
		
		return null;
	}

	/**
	 * Loads the specified sample data file into the embedded MongoDB instance.
	 * @param path the path to the file
	 * @param collection the DB collection into which the data is saved
	 */
	protected void loadSampleData(String path, String collection) {
		JSONArray jsonArray = readJSONFile(path);
		for (Object object : jsonArray) {
			mongoTemplate.save(object.toString(), collection);
		}
		
	}

	/**
	 * Unit tests can load the data in setup(), but it may be necessary to reset between tests, so
	 * that read/writes don't affect test expectations. 
	 * 
	 * @param collection the collection to remove
	 */
	protected void removeCollection(String collection) {
		mongoTemplate.remove(new Query(), collection);
	}

	protected DBCollection getCollection(String collection) {
		return mongoTemplate.getCollection(collection);
	}

}