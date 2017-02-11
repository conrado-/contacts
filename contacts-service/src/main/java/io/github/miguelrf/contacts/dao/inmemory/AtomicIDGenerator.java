package io.github.miguelrf.contacts.dao.inmemory;

import java.util.concurrent.atomic.AtomicLong;

import com.google.inject.Singleton;

@Singleton
public class AtomicIDGenerator {

	private AtomicLong id = new AtomicLong();

	public String generateID() {
		return "" + id.getAndIncrement();
	}
}
