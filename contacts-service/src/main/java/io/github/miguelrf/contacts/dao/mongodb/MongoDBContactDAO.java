package io.github.miguelrf.contacts.dao.mongodb;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import jersey.repackaged.com.google.common.collect.Lists;

import org.mongojack.DBCursor;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mongodb.DBCollection;

import io.github.miguelrf.contacts.model.Contact;
import io.github.miguelrf.contacts.model.Assignemet;
import io.github.miguelrf.contacts.dao.ContactDAO;
import io.github.miguelrf.contacts.dao.exception.RepositoryException;

//@Singleton//TODO doesn't work with guice-bundle. see https://github.com/HubSpot/dropwizard-guice/issues/40
public class MongoDBContactDAO implements ContactDAO {

	private JacksonDBCollection<Contact, String> col;

	@Inject
	public MongoDBContactDAO(DBCollection contactsCollection, ObjectMapper objectMapper) {
		col = JacksonDBCollection.wrap(contactsCollection, Contact.class, String.class, objectMapper);
	}

	@Override
	public List<Contact> getAllContacts() {
		DBCursor<Contact> cursor = col.find();
		List<Contact> list = Lists.newArrayList((Iterator<Contact>) cursor);
		return list;
	}

	@Override
	public Optional<Contact> getContact(String contactId) {
		Contact contact = col.findOneById(contactId);
		return Optional.ofNullable(contact);
	}

	@Override
	public List<Contact> getContacts(int limit, int offset, Optional<String> search) {
		DBCursor<Contact> cursor = getSearchCursor(search).skip(offset).limit(limit);
		List<Contact> list = Lists.newArrayList((Iterator<Contact>) cursor);
		return list;
	}

	private DBCursor<Contact> getSearchCursor(Optional<String> search) {
		if (search.isPresent()) {
			Pattern pattern = Pattern.compile(".*" + search.get().toLowerCase() + ".*", Pattern.CASE_INSENSITIVE);
			return col.find(DBQuery.regex(Contact.NAME, pattern));
		} else {
			return col.find();
		}
	}

	@Override
	public List<Assignemet> getAllProjectDays(String contactId) {
		Optional<Contact> contact = getContact(contactId);
		if (contact.isPresent()) {
			return contact.get().getProjectDays();
		} else {
			throw new RepositoryException("Contact " + contactId + " doesn't exist.");
		}
	}

	@Override
	public Contact createContact(String name) {
		Contact contact = new Contact(name);
		WriteResult<Contact, String> result = col.insert(contact);
		Contact contactWithId = col.findOneById(result.getSavedId());
		return contactWithId;
	}

	@Override
	public void updateContact(String id, String name) {
		Contact contact = new Contact(name);
		col.updateById(id, contact);
	}

	@Override
	public void save(Contact contact) {
		col.insert(contact);
	}

	@Override
	public void saveAll(List<Contact> contacts) {
		col.insert(contacts);
	}

	@Override
	public void deleteContact(String contactId) {
		col.removeById(contactId);
	}

	@Override
	public long getContactCount(Optional<String> usedSearch) {
		return getSearchCursor(usedSearch).count();
	}

	@Override
	public void deleteAllContacts() {
		col.drop();
	}

}
