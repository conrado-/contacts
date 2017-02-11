package io.github.miguelrf.contacts.rest.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import io.github.miguelrf.contacts.model.Contact;
import io.github.miguelrf.contacts.model.Assignemet;
import io.github.miguelrf.contacts.rest.responses.ContactResponse;
import io.github.miguelrf.contacts.rest.responses.ContactsResponse;
import io.github.miguelrf.contacts.rest.responses.ProjectDaysResponse;

public class EntityMapper {

	@Inject
	private UriInfo context;

	public ContactsResponse mapToContactsResponse(List<Contact> contacts, int limit, int offset, long totalCount,
			Optional<String> search) {
		List<ContactResponse> contactResponses = contacts.stream().map((contact) -> mapToRContact(contact))
				.collect(Collectors.toList());
		return new ContactsResponse(limit, offset, totalCount, contactResponses, search, context.getBaseUri());
	}

	public ContactResponse mapToRContact(Contact input) {
		return new ContactResponse(input.getId(), input.getName(), context.getBaseUri());
	}

	public List<ProjectDaysResponse> mapToRProjectDays(List<Assignemet> projectDays) {
		return projectDays.stream().map((projectDay) -> mapToRProjectDay(projectDay)).collect(Collectors.toList());
	}

	public ProjectDaysResponse mapToRProjectDay(Assignemet input) {
		return new ProjectDaysResponse(input.getMonth(), input.getDaysCount());
	}
}
