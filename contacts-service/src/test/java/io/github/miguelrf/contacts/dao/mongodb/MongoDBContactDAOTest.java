package io.github.miguelrf.contacts.dao.mongodb;

import static org.junit.Assert.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import jersey.repackaged.com.google.common.collect.Lists;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.miguelrf.contacts.di.pure.GuiceRunner;
import io.github.miguelrf.contacts.model.Contact;
import io.github.miguelrf.contacts.model.Assignemet;

@RunWith(GuiceRunner.class)
@Ignore("mongodb needed. migrate to integration test. docker will start mongodb automatically.")
public class MongoDBContactDAOTest {

	@Inject
	private MongoDBContactDAO dao;

	// TODO close mongoClient

	@Before
	public void init() {
		dao.deleteAllContacts();
	}

	@Test
	public void find() {
		String name = "Albert Stark";
		Contact contact = new Contact(name);
		YearMonth yearMonth = YearMonth.now();
		contact.addProjectDays(new Assignemet(yearMonth, 5));

		dao.save(contact);
		List<Contact> contacts = dao.getAllContacts();

		assertEquals(1, contacts.size());
		Contact albert = contacts.get(0);
		assertEquals(name, albert.getName());
		List<Assignemet> projectDays = albert.getProjectDays();
		assertEquals(1, projectDays.size());
		assertEquals(yearMonth, projectDays.get(0).getMonth());
	}

	@Test
	public void countWithSearch() {
		ArrayList<Contact> contacts = Lists.newArrayList(
				new Contact("Albert Stark"),
				new Contact("Peter Müller"),
				new Contact("Paul Köhler"));
		dao.saveAll(contacts);

		long count = dao.getContactCount(Optional.of("P"));
		assertEquals(2, count);
	}

	@Test
	public void createContact() {
		Contact contact = dao.createContact("Neuer Contact");
		assertThat(dao.getAllContacts().size(), Is.is(1));
		assertThat(contact.getId(), IsNull.notNullValue());
	}

}
