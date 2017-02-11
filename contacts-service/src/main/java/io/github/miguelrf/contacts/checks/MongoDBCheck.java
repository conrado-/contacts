package io.github.miguelrf.contacts.checks;

import javax.inject.Inject;

import com.hubspot.dropwizard.guice.InjectableHealthCheck;
import com.mongodb.DBCollection;

import io.github.miguelrf.contacts.configuration.ContactsConfiguration;

public class MongoDBCheck extends InjectableHealthCheck {

	@Inject
	private ContactsConfiguration config;
	@Inject
	private DBCollection collection;

	@Override
	protected Result check() throws Exception {
		try {
			collection.count();
			return Result.healthy();
		} catch (Exception e) {
			return Result.unhealthy(new RuntimeException("Couldn't connect to mongoDB. Config: " + config.getMongoDB(),
					e));
		}
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

}
