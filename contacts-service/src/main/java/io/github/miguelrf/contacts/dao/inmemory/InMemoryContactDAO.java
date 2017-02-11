package io.github.miguelrf.contacts.dao.inmemory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.miguelrf.contacts.model.Contact;
import io.github.miguelrf.contacts.model.Assignemet;
import io.github.miguelrf.contacts.dao.ContactDAO;
import io.github.miguelrf.contacts.dao.exception.RepositoryException;

@Singleton
public class InMemoryContactDAO implements ContactDAO {

	private Map<String, Contact> contacts;
	@Inject
	private AtomicIDGenerator idGenerator;

	@Override
	public List<Contact> getAllContacts() {
		return (List<Contact>) contacts.values();
	}

	@Override
	public List<Contact> getContacts(int limit, int offset, Optional<String> search) {
		Collection<Contact> values = contacts.values();
		Stream<Contact> contactStream = values.stream();
		if (search.isPresent()) {
			contactStream = contactStream.filter(CommonPredicates.searchName(search.get()));
		}
		contactStream = contactStream
				.skip(offset)
				.limit(limit);
		return contactStream.collect(Collectors.toList());
	}

	@Override
	public long getContactCount(Optional<String> usedSearch) {
		if (usedSearch.isPresent()) {
			return contacts.values().stream()
					.filter(CommonPredicates.searchName(usedSearch.get())).count();
		} else {
			return contacts.size();
		}
	}

	@Override
	public Optional<Contact> getContact(String contactId) {
		Contact contact = contacts.get(contactId);
		return Optional.ofNullable(contact);
	}

	@Override
	public List<Assignemet> getAllProjectDays(String contactId) {
		Optional<Contact> contact = getContact(contactId);
		if (contact.isPresent()) {
			return contact.get().getProjectDays();
		}
		throw new RepositoryException("Invalid contact id " + contactId);
	}

	@Override
	public Contact createContact(String name) {
		String id = idGenerator.generateID();
		Contact contact = new Contact(name);
		contacts.put(id, contact);
		return contact;
	}

	@Override
	public void updateContact(String id, String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(Contact contact) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void saveAll(List<Contact> contactList) {
		contacts = contactList.stream().collect(Collectors.toMap(Contact::getId, Function.identity()));
	}

	@Override
	public void deleteContact(String contactId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAllContacts() {
		contacts.clear();
	}

}
