package org.johnnybionic.database;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.johnnybionic.BasicSpringEmbeddedMongoApplication;
import org.johnnybionic.domain.CoffeeShop;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * A base class for database tests.
 * 
 * Ensure that no specific types are returned - i.e. anything that is
 * bound either to MongoTemplate or the MongoDB Java driver. That way. it's
 * straightforward to swap between, or even mix, the two.
 * 
 * @author johnny
 *
 */

@ActiveProfiles({"junit"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BasicSpringEmbeddedMongoApplication.class)
public abstract class AbstractDatabaseTest<T> {

	
	protected static final String COFFEESHOPS_COLLECTION = "coffeeshops";
	protected static final String LOCATION = "location";
	
	// mixing both ways for accessing - basically to see how the template works
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoDatabase mongoDatabase;

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
	 * An index is mandatory for geographic queries
	 * 
	 * IndexInfo [indexFields=[IndexField [ key: location, direction: null, type: GEO, weight: NaN]], name=location_2dsphere, unique=false, dropDuplicates=false, sparse=false, language=]]
	 * 
	 * @param collection the collection to which the index is added
	 * @param fieldName the name of the geographical field
	 * @return 
	 */
	protected String createIndex(String collection, String fieldName) {
		GeospatialIndex indexDefinition = new GeospatialIndex(fieldName).typed(GeoSpatialIndexType.GEO_2DSPHERE);
		mongoTemplate.indexOps(collection).ensureIndex(indexDefinition);

		//MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
		//String createIndex = mongoCollection.createIndex(new Document("location", "2dsphere"));
		
		List<IndexInfo> indexInfo = mongoTemplate.indexOps(collection).getIndexInfo();
		
		return indexInfo.toString();
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

	protected long getCountForCollection(String collection) {
		// TODO Auto-generated method stub
		return mongoTemplate.getCollection(collection).count();
	}

	protected T findById(String id, Class<T> class1, String collectionName) {
		return mongoTemplate.findById(id, class1, collectionName);
	}

	protected T findOne(Query query, Class<T> class1, String collectionName) {
		return mongoTemplate.findOne(query, class1, collectionName);
	}

	protected List<T> findAll(Class<T> class1, String collectionName) {
		return mongoTemplate.findAll(class1, collectionName);
	}

}