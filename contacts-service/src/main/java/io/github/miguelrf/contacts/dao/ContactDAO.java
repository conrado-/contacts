package io.github.miguelrf.contacts.dao;

import java.util.List;
import java.util.Optional;

import io.github.miguelrf.contacts.model.Contact;
import io.github.miguelrf.contacts.model.Assignemet;

public interface ContactDAO {

	List<Contact> getAllContacts();

	Optional<Contact> getContact(String contactId);

	List<Contact> getContacts(int limit, int offset, Optional<String> search);

	List<Assignemet> getAllProjectDays(String contactId);

	Contact createContact(String name);

	void updateContact(String id, String name);

	void save(Contact contact);

	void saveAll(List<Contact> contacts);

	void deleteContact(String contactId);

	long getContactCount(Optional<String> usedSearch);

	void deleteAllContacts();

}
