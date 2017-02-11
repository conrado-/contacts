package io.github.miguelrf.contacts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Throwables;
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;

public class MongoDbTestUtil {

	@Inject
	private DBCollection contactsCollection;

	public void writeJsonFileToDb(Path file) {
		try {
			byte[] allBytes = Files.readAllBytes(file);
			String json = new String(allBytes);
			writeJsonStringToDb(json);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void writeJsonStringToDb(String json) {
		BasicDBList dbObject = (BasicDBList) JSON.parse(json);
		contactsCollection.insert((List) dbObject);
	}

	public void clearContacts() {
		contactsCollection.drop();
	}

}
