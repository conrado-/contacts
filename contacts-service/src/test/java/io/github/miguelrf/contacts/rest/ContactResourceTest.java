package io.github.miguelrf.contacts.rest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import io.dropwizard.client.JerseyClientBuilder;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hamcrest.core.IsNull;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.github.miguelrf.contacts.di.service.ServiceTestRunner;
import io.github.miguelrf.contacts.rest.util.MediaTypeWithCharset;

@RunWith(ServiceTestRunner.class)
@Ignore("mongodb needed. migrate to integration test. docker will start mongodb automatically.")
public class ContactResourceTest extends AbstractContactResourceTest {

	// TODO use rest-assured instead

	@Test
	public void getAllContacts() {
		Response response = client.request().get();
		JsonNode json = response.readEntity(JsonNode.class);

		assertThat(response.getStatus(), equalTo(200));
		assertThat(json.get("totalCount").asInt(), equalTo(10));
	}

	@Test
	public void getAllContactsSearch() {
		Response response = client.queryParam("search", "Paul").request().get();
		JsonNode json = response.readEntity(JsonNode.class);

		assertThat(response.getStatus(), equalTo(200));
		assertThat(json.get("totalCount").asInt(), equalTo(2));
	}

	@Ignore("we now using the mongodb-id instead of our own id. hence, '0' doesn't work any longer")
	@Test
	public void getAllContact() {
		Response response = client.path("0").request().get();
		JsonNode json = response.readEntity(JsonNode.class);

		assertThat(response.getStatus(), equalTo(200));
		assertThat(json.get("name").asText(), equalTo("Paul Persch"));
	}

	@Ignore("we now using the mongodb-id instead of our own id. hence, '0' doesn't work any longer")
	@Test
	public void getAllProjectDays() {
		Response response = client.path("0/projectdays").request().get();
		ArrayNode json = response.readEntity(ArrayNode.class);

		assertThat(response.getStatus(), equalTo(200));
		assertThat(json.size(), is(5));
		for (JsonNode projectDay : json) {
			assertThat(projectDay.hasNonNull("month"), is(true));
			assertThat(projectDay.hasNonNull("daysCount"), is(true));
		}
	}

	@Test
	public void createContact() {
		String contactName = "Test Contact";
		Entity<String> jsonEntity = createContactJsonEntity(contactName);
		Response creationResponse = client.request(MediaTypeWithCharset.APPLICATION_JSON_UTF8).post(jsonEntity);

		assertThat(creationResponse.getStatus(), is(201));
		URI urlOfNewContact = creationResponse.getLocation();
		assertThat(urlOfNewContact, IsNull.notNullValue());

		Response getResponse = new JerseyClientBuilder(RULE.getEnvironment()).build("create test client").target(
				urlOfNewContact).request().get();
		assertThat(getResponse.getStatus(), equalTo(200));
		JsonNode contactJson = getResponse.readEntity(JsonNode.class);
		assertThat(contactJson.get("name").asText(), equalTo(contactName));
	}

	private Entity<String> createContactJsonEntity(String name) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JsonGenerator generator = Json.createGenerator(out);
		generator.writeStartObject()
				.write("name", name)
				.writeEnd()
				.close();
		String json = new String(out.toByteArray());
		Entity<String> testEntity = Entity.entity(json, MediaType.APPLICATION_JSON);
		return testEntity;
	}
}
