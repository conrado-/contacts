package io.github.miguelrf.contacts;

import java.net.UnknownHostException;

import org.mongojack.internal.MongoJackModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import io.github.miguelrf.contacts.configuration.MongoDBConfig;
import io.github.miguelrf.contacts.configuration.ContactsConfiguration;
import io.github.miguelrf.contacts.dao.ContactDAO;
import io.github.miguelrf.contacts.dao.exception.RepositoryException;
import io.github.miguelrf.contacts.dao.mongodb.MongoDBContactDAO;
import io.github.miguelrf.contacts.util.ser.Java8TimeModule;

public class ContactsModule extends AbstractModule {

	private ObjectMapper objectMapper;

	private static Logger logger = LoggerFactory.getLogger(ContactsModule.class);

	public ContactsModule(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		objectMapper.registerModule(new Java8TimeModule());
		MongoJackModule.configure(objectMapper);
	}

	@Override
	protected void configure() {
		//mongodb
		bind(ContactDAO.class).to(MongoDBContactDAO.class);
		// bind(ContactDAO.class).to(InMemoryContactDAO.class);
	}

	@Provides
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	//@Singleton//TODO doesn't work with guice-bundle. see https://github.com/HubSpot/dropwizard-guice/issues/40
	//TODO use connection pooling (jetty datasource)
	@Provides
	public DBCollection get(ContactsConfiguration config) {
		try {
			MongoClientOptions options = new MongoClientOptions.Builder().connectTimeout(1000 * 1).build();
			MongoDBConfig mongoDBConfig = config.getMongoDB();
			logger.info("Using mongodb configuration: {}", mongoDBConfig.toString());
			ServerAddress serverAddress = new ServerAddress(mongoDBConfig.getHost(), mongoDBConfig.getPort());
			MongoClient mongoClient = new MongoClient(serverAddress, options);
			DB personDb = mongoClient.getDB(mongoDBConfig.getDatabaseName());
			DBCollection contactsCollection = personDb.getCollection(mongoDBConfig.getContactCollectionName());
			return contactsCollection;
		} catch (UnknownHostException e) {
			throw new RepositoryException("Couldn't create MongoDB Client.", e);
		}
	}

}
