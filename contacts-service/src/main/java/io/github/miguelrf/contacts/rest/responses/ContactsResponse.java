package io.github.miguelrf.contacts.rest.responses;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.ApiModelProperty;

import io.github.miguelrf.contacts.rest.URLConstants;

/**
 * data & metadata
 *
 */
@XmlRootElement
public class ContactsResponse {

	@ApiModelProperty(value = "the total amount of contacts matching the request.")
	private long totalCount;
	private List<ContactResponse> contacts;
	private int limit;
	private int offset;
	private Optional<String> search;
	private URI baseUri;

	public ContactsResponse(int limit, int offset, long totalContactCount, List<ContactResponse> contacts,
			Optional<String> search, URI baseUri) {
		this.limit = limit;
		this.offset = offset;
		this.totalCount = totalContactCount;
		this.contacts = contacts;
		this.search = search;
		this.baseUri = baseUri;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public String getSearch() {
		return search.isPresent() ? search.get() : "";
	}

	public List<ContactResponse> getContacts() {
		return contacts;
	}

	@XmlElement(name = "links")
	public List<LinkResponse> getLinks() {
		int nextOffset = offset + limit;
		URI nextUrl = createUrl(limit, nextOffset, search);
		LinkResponse nextPage = new LinkResponse("nextPage", nextUrl);

		List<LinkResponse> links = Lists.newArrayList(nextPage);

		int previousOffset = offset - limit;
		if (previousOffset >= 0) {
			URI previousUrl = createUrl(limit, previousOffset, search);
			LinkResponse previousPage = new LinkResponse("previousPage", previousUrl);
			links.add(previousPage);
		}
		return links;
	}

	private URI createUrl(int limit, int offset, Optional<String> search) {
		String nextUrl = URLConstants.EMPLOYEES_RELATIVE_URL + "?limit=" + limit + "&offset=" + offset;
		if (search.isPresent()) {
			nextUrl += "&search=" + search.get();
		}
		return baseUri.resolve(nextUrl);
	}
}
