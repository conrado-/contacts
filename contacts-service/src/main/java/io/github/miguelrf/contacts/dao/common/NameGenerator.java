package io.github.miguelrf.contacts.dao.common;

import java.util.Random;

public class NameGenerator {

	private static String[] firstnames = { "John", "Juan"};
	private static String[] surnames = { "Doe", "Perez"};
	private static Random random = new Random();

	public static String generateName() {
		int firstnameIndex = randomInt(0, firstnames.length);
		String firstname = firstnames[firstnameIndex];

		int lastnameIndex = randomInt(0, surnames.length);
		String lastname = surnames[lastnameIndex];

		return firstname + " " + lastname;
	}

	private static int randomInt(int minInclusive, int maxExclusive) {
		return random.nextInt(maxExclusive - minInclusive) + minInclusive;
	}
}
