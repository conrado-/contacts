package io.github.miguelrf.contacts.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import io.github.miguelrf.contacts.model.Contact;
import io.github.miguelrf.contacts.model.Assignemet;
import io.github.miguelrf.contacts.dao.ContactDAO;
import io.github.miguelrf.contacts.rest.exception.ContactsClientErrorException;
import io.github.miguelrf.contacts.rest.request.ContactData;
import io.github.miguelrf.contacts.rest.responses.ContactResponse;
import io.github.miguelrf.contacts.rest.responses.ContactsResponse;
import io.github.miguelrf.contacts.rest.responses.ProjectDaysResponse;
import io.github.miguelrf.contacts.rest.util.EntityMapper;
import io.github.miguelrf.contacts.rest.util.MediaTypeWithCharset;
import io.github.miguelrf.contacts.util.ser.DummyDataInitializer;

@Api(value = URLConstants.EMPLOYEES, description = "REST API to interact with contact resources.")
@Path(URLConstants.EMPLOYEES)
public class ContactResource {

	@Context
	private UriInfo context;
	@Context
	private HttpHeaders headers;
	@Inject
	private ContactDAO dao;
	@Inject
	private EntityMapper mapper;
	@Inject
	private DummyDataInitializer dummyInitializer;

	// TODO better http status code on failures, illegal inputs, exceptions

	/*
	 * GET, Read
	 */

	@ApiOperation(value = "Get all contacts", notes = "Interact with contact resources", response = ContactsResponse.class)
	@GET
	@Path("/")
	@Produces(MediaTypeWithCharset.APPLICATION_JSON_UTF8)
	public ContactsResponse getAllContacts(
			@ApiParam(value = "limits the result set", required = false, defaultValue = "10") @QueryParam("limit") Integer limit,
			@ApiParam(value = "the offset", required = false, defaultValue = "0") @QueryParam("offset") Integer offset,
			@ApiParam(value = "search for names that contains the given string. not case sensetive. ", required = false) @QueryParam("search") String search) {
		initDummyDataIfDatabaseEmpty();
		int usedLimit = limit == null ? 10 : limit;
		int usedOffset = offset == null ? 0 : offset;
		Optional<String> usedSearch = Optional.ofNullable(search);
		List<Contact> contacts = dao.getContacts(usedLimit, usedOffset, usedSearch);
		long totalCount = dao.getContactCount(usedSearch);
		ContactsResponse response = mapper.mapToContactsResponse(contacts, usedLimit, usedOffset, totalCount,
				usedSearch);
		return response;
	}

	private void initDummyDataIfDatabaseEmpty() {
		long contactCount = dao.getContactCount(Optional.empty());
		if (contactCount == 0){
			dummyInitializer.initDummyData();
		}
	}

	@ApiOperation(value = "Get an contacts wit a certain ID", response = ContactResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid ID supplied"),
	})
	@GET
	@Path("/{contactId}")
	@Produces(MediaTypeWithCharset.APPLICATION_JSON_UTF8)
	public ContactResponse getContact(
			@ApiParam(value = "the id of the contact", required = true) @PathParam("contactId") String contactId) {
		Optional<Contact> contact = dao.getContact(contactId);
		if (contact.isPresent()) {
			ContactResponse rContact = mapper.mapToRContact(contact.get());
			return rContact;
		}
		throw new ContactsClientErrorException("No Contact with id " + contactId + " found.", 9);
	}

	@ApiOperation(value = "Get the project day for an contact", response = ProjectDaysResponse.class)
	@GET
	@Path("/{contactId}/projectdays/")
	@Produces(MediaTypeWithCharset.APPLICATION_JSON_UTF8)
	public List<ProjectDaysResponse> getAllProjectDays(
			@PathParam("contactId") String contactId) {
		List<Assignemet> projectDays = dao.getAllProjectDays(contactId);
		List<ProjectDaysResponse> rProjectDays = mapper.mapToRProjectDays(projectDays);
		return rProjectDays;
	}

	/*
	 * POST, Create
	 */

	@ApiOperation(value = "Creates a new contact")
	@POST
	@Path("/")
	@Consumes(MediaTypeWithCharset.APPLICATION_JSON_UTF8)
	public Response createContact(
			@ApiParam(value = "contact data", required = true) ContactData newContactData)
			throws URISyntaxException {
		Contact contact = dao.createContact(newContactData.getName());

		URI uri = createNewLocationURI(contact.getId());
		Response response = Response.created(uri).build();

		return response;
	}

	private URI createNewLocationURI(String contactId) throws URISyntaxException {
		String uriString = context.getAbsolutePath().toString();
		if (!uriString.endsWith("/")) {
			uriString += "/";
		}
		return new URI(uriString + contactId);
	}

	/*
	 * PUT, Update
	 */

	@ApiOperation(value = "Updates a given contact")
	@PUT
	@Path("/{contactId}")
	@Consumes(MediaTypeWithCharset.APPLICATION_JSON_UTF8)
	public Response updateContact(
			@ApiParam(value = "the id of the contact to be updated") @PathParam("contactId") String contactId,
			@ApiParam(value = "the new contact data", required = true) ContactData newContactData) {
		String name = newContactData.getName();
		dao.updateContact(contactId, name);
		return Response.ok().build();
	}

	/*
	 * DELETE, delete
	 */

	@ApiOperation(value = "Deletes an contact")
	@DELETE
	@Path("/{contactId}")
	public Response deleteContact(@PathParam("contactId") String contactId) {
		dao.deleteContact(contactId);
		return Response.ok().build();
	}

}
