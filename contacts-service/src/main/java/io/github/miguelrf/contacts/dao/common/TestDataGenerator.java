package io.github.miguelrf.contacts.dao.common;

import java.time.YearMonth;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.inject.Singleton;

import io.github.miguelrf.contacts.model.Contact;
import io.github.miguelrf.contacts.model.Assignemet;

@Singleton
public class TestDataGenerator {

	public List<Contact> createContacts(int amount) {
		List<Contact> contacts = IntStream.rangeClosed(1, amount)
				.mapToObj((index) -> createContact())
				.collect(Collectors.toList());
		return contacts;
	}

	private Contact createContact() {
		Contact contact = new Contact(NameGenerator.generateName());
		List<Assignemet> bonuses = createRandomProjectDays(contact);
		contact.addProjectDays(bonuses);
		return contact;
	}

	private List<Assignemet> createRandomProjectDays(Contact contact) {
		int bonusCount = randomInt(0, 12);
		List<Assignemet> projectDays = IntStream.rangeClosed(0, bonusCount).mapToObj((index) -> {
			int days = randomInt(1, 30);
			int month = randomInt(1, 12);
			YearMonth months = YearMonth.of(2015, month);
			Assignemet projectDay = new Assignemet(months, days);
			return projectDay;
		}).collect(Collectors.toList());
		return projectDays;
	}

	private int randomInt(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}
}
