package io.github.miguelrf.contacts.di.pure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

import io.github.miguelrf.contacts.ContactsModule;
import io.github.miguelrf.contacts.configuration.MongoDBConfig;
import io.github.miguelrf.contacts.configuration.ContactsConfiguration;

public class TestGuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		ObjectMapper objectMapper = new ObjectMapper();
		install(new ContactsModule(objectMapper));

		ContactsConfiguration config = createTestConfiguration();
		bind(ContactsConfiguration.class).toInstance(config);
	}

	private ContactsConfiguration createTestConfiguration() {
		MongoDBConfig mongoDBConfig = new MongoDBConfig();
		mongoDBConfig.setDatabaseName("test");
		mongoDBConfig.setContactCollectionName("contactsTest");
		ContactsConfiguration config = new ContactsConfiguration();
		config.setMongoDB(mongoDBConfig);
		return config;
	}

}
