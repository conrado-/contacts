package io.github.miguelrf.contacts.rest.exception;

import javax.ws.rs.WebApplicationException;

import io.github.miguelrf.contacts.rest.responses.ErrorResponse;

@SuppressWarnings("serial")
// @ApplicationException
public class ContactsClientErrorException extends WebApplicationException {

	private long errorId;
	private String message;

	public ContactsClientErrorException(String message, long errorId) {
		this.message = message;
		this.errorId = errorId;
	}

	public ContactsClientErrorException(Throwable throwable, long errorId) {
		Throwable causeException = throwable.getCause();
		message = causeException != null ? causeException.getMessage() : throwable.getMessage();
		this.errorId = errorId;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public long getErrorId() {
		return errorId;
	}

	public ErrorResponse createErrorResponse() {
		return new ErrorResponse(getMessage(), errorId);
	}
}
