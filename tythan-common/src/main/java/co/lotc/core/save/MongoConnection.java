package co.lotc.core.save;

import java.util.Map;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;

import lombok.AccessLevel;
import lombok.Getter;

public class MongoConnection implements AutoCloseable{
	@Getter(AccessLevel.PACKAGE) private final ClientSession session;
	@Getter private final MongoDatabase database;
	
	MongoConnection(MongoClient client, String dbName, CodecRegistry codecs) {
		database = client.getDatabase(dbName).withCodecRegistry(codecs);
		session = client.startSession();
		session.startTransaction();
	}

	public MongoCollectionBuilder collection(String name) {
		return new MongoCollectionBuilder(this, name);
	}
	
	public void insert(String collectionName, Map<String, Object> map) {
		database.getCollection(collectionName).insertOne(session, new Document(map));
	}
	
	public void replace(String collectionName, Map<String, Object> criteria, Map<String, Object> map) {
		database.getCollection(collectionName).replaceOne(session, new Document(criteria), new Document(map), new ReplaceOptions().upsert(true));
	}
	
	public void delete(String collectionName, Map<String, Object> map) {
		database.getCollection(collectionName).deleteMany(session, new Document(map));
	}
	
	public void update(String collectionName, Map<String, Object> criteria, Map<String, Object> map) {
		database.getCollection(collectionName).updateMany(session, new Document(criteria), new Document(map));
	}

	@Override
	public void close(){
		session.commitTransaction();
		session.close();
	}
	
}
