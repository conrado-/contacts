package io.github.miguelrf.contacts.di.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

import io.github.miguelrf.contacts.ContactsModule;
import io.github.miguelrf.contacts.configuration.ContactsConfiguration;

public class ServiceTestModule extends AbstractModule {

	private ContactsConfiguration config;

	public ServiceTestModule(ContactsConfiguration config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		ObjectMapper objectMapper = new ObjectMapper();
		install(new ContactsModule(objectMapper));
		bind(ContactsConfiguration.class).toInstance(config);
	}

}