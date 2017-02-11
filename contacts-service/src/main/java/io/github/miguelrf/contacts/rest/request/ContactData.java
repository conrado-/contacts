package io.github.miguelrf.contacts.rest.request;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlRootElement
public class ContactData {

	@ApiModelProperty(required = true)
	private String name;

	public String getName() {
		return name;
	}

}
