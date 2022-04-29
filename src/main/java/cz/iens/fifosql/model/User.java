package cz.iens.fifosql.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String guid;
	private String name;

	public User(Long id, String guid, String name) {
		super();
		this.id = id;
		this.guid = guid;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", guid=" + guid + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(guid, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(guid, other.guid) && Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
}
