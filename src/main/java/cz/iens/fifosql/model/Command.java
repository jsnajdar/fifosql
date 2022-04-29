package cz.iens.fifosql.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Command implements Serializable {

	private static final long serialVersionUID = 1L;

	private final UUID guid;
	private final CommandMethod method;
	private final CommandPayloadType payloadType;
	private final Serializable payload;

	public Command(UUID guid, CommandMethod method, CommandPayloadType payloadType, Serializable payload) {
		super();
		this.guid = guid;
		this.method = method;
		this.payloadType = payloadType;
		this.payload = payload;
	}

	public UUID getGuid() {
		return guid;
	}
	public CommandMethod getMethod() {
		return method;
	}
	
	public CommandPayloadType getPayloadType() {
		return payloadType;
	}
	public Serializable getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return "Command [guid=" + guid + ", method=" + method + ", payloadType=" + payloadType + ", payload=" + payload
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(guid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		return Objects.equals(guid, other.guid);
	}
	
}
