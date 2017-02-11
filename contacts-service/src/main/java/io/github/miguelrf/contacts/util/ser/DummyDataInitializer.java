package io.github.miguelrf.contacts.util.ser;

import java.util.List;

import javax.inject.Inject;

import io.github.miguelrf.contacts.model.Contact;
import io.github.miguelrf.contacts.dao.ContactDAO;
import io.github.miguelrf.contacts.dao.common.TestDataGenerator;

public class DummyDataInitializer {

	@Inject
	private TestDataGenerator generator;
	@Inject
	private ContactDAO dao;

	public void initDummyData() {
		dao.deleteAllContacts();
		List<Contact> contacts = generator.createContacts(500);
		dao.saveAll(contacts);
	}
}
