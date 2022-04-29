package cz.iens.fifosql.builder;

import cz.iens.fifosql.model.User;

public class UserBuilder {

	private Long id = 0L;
	private String guid = "";
	private String name = "";
	
	public UserBuilder() {
		super();
	}

	public UserBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public UserBuilder guid(String guid) {
		this.guid = guid;
		return this;
	}

	public UserBuilder name(String name) {
		this.name = name;
		return this;
	}

	public User build() {
		return new User(id, guid, name);
	}
}
