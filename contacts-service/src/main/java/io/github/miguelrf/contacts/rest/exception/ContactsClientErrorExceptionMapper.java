package io.github.miguelrf.contacts.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.github.miguelrf.contacts.rest.responses.ErrorResponse;
import io.github.miguelrf.contacts.rest.util.MediaTypeWithCharset;

@Provider
public class ContactsClientErrorExceptionMapper implements ExceptionMapper<ContactsClientErrorException> {

	@Override
	public Response toResponse(ContactsClientErrorException exception) {
		ErrorResponse errorResponse = exception.createErrorResponse();
		return Response.status(Status.BAD_REQUEST).type(MediaTypeWithCharset.APPLICATION_JSON_UTF8)
				.entity(errorResponse).build();
	}

}
