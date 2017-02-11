package io.github.miguelrf.contacts.configuration;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MongoDBConfig {

	@NotEmpty
	private String databaseName;

	@NotEmpty
	private String contactCollectionName;

	private int port = Integer.parseInt(System.getProperty("dbPort", "27017"));

	private String host = System.getProperty("dbHost", "localhost");

	@JsonProperty
	public String getDatabaseName() {
		return databaseName;
	}

	@JsonProperty
	public String getContactCollectionName() {
		return contactCollectionName;
	}

	@JsonProperty
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@JsonProperty
	public void setContactCollectionName(String contactCollectionName) {
		this.contactCollectionName = contactCollectionName;
	}

	@JsonProperty
	public int getPort() {
		return port;
	}

	@JsonProperty
	public void setPort(int port) {
		this.port = port;
	}

	@JsonProperty
	public String getHost() {
		return host;
	}

	@JsonProperty
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String toString() {
		return "MongoDBConfig [databaseName=" + databaseName + ", contactCollectionName=" + contactCollectionName
				+ ", port=" + port + ", host=" + host + "]";
	}

}
