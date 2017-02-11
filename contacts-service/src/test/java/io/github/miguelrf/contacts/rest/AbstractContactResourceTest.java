package io.github.miguelrf.contacts.rest;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.nio.file.Paths;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import io.github.miguelrf.contacts.MongoDbTestUtil;
import io.github.miguelrf.contacts.ContactsApplication;
import io.github.miguelrf.contacts.configuration.ContactsConfiguration;

public class AbstractContactResourceTest {

	@ClassRule
	public static final DropwizardAppRule<ContactsConfiguration> RULE =
			new DropwizardAppRule<ContactsConfiguration>(ContactsApplication.class,
					ResourceHelpers.resourceFilePath("test-config.yml"));

	protected static WebTarget client;

	@Inject
	private MongoDbTestUtil testUtil;

	@BeforeClass
	public static void initClient() {
		String baseUrl = String.format("http://localhost:%d/contacts", RULE.getLocalPort());
		client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client").target(baseUrl);
	}

	@Before
	public void init() {
		testUtil.clearContacts();
		testUtil.writeJsonFileToDb(Paths.get("src/test/resources/contacts_10.json"));
	}

}
