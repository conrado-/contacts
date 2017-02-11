package io.github.miguelrf.contacts.dao.inmemory;

import java.util.function.Predicate;

import io.github.miguelrf.contacts.model.Contact;

public class CommonPredicates {

	public static Predicate<Contact> searchName(String searchString) {
		return contact -> contact.getName().toLowerCase().contains(searchString.toLowerCase());
	}

}
