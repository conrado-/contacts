package io.github.miguelrf.contacts;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.github.miguelrf.contacts.configuration.ContactsConfiguration;

public class ContactsApplication extends Application<ContactsConfiguration> {

	private GuiceBundle<ContactsConfiguration> guiceBundle;
	private ContactsModule module;

	public static void main(String[] args) throws Exception {
		new ContactsApplication().run(args);
	}

	@Override
	public String getName() {
		return "contacts";
	}

	@Override
	public void initialize(Bootstrap<ContactsConfiguration> bootstrap) {
		ObjectMapper objectMapper = bootstrap.getObjectMapper();
		module = new ContactsModule(objectMapper);

		guiceBundle = GuiceBundle.<ContactsConfiguration> newBuilder()
				.addModule(module)
				.setConfigClass(ContactsConfiguration.class)
				.enableAutoConfig(getClass().getPackage().getName())
				.build();
		bootstrap.addBundle(guiceBundle);

		//accessible via http://localhost:<your_port>/swagger
		bootstrap.addBundle(new SwaggerBundle<ContactsConfiguration>() {
			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ContactsConfiguration configuration) {
				return configuration.swaggerBundleConfiguration;
			}
		});
	}

	@Override
	public void run(ContactsConfiguration configuration, Environment environment) {
		// resources, healthchecks etc are automatically configured via guice bundle (enableAutoConfig())
	}

}
