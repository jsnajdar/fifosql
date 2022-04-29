package cz.iens.fifosql.builder;

import java.io.Serializable;
import java.util.UUID;

import cz.iens.fifosql.model.Command;
import cz.iens.fifosql.model.CommandMethod;
import cz.iens.fifosql.model.CommandPayloadType;

public class CommandBuilder {

	private UUID uuid = UUID.randomUUID();
	private CommandMethod method = null;
	private CommandPayloadType payloadType = null;
	private Serializable payload = null;
	
	public CommandBuilder method(CommandMethod method) {
		this.method = method;
		return this;
	}

	public CommandBuilder payloadType(CommandPayloadType payloadType) {
		this.payloadType = payloadType;
		return this;
	}

	public CommandBuilder payload(Serializable payload) {
		this.payload = payload;
		return this;
	}
	
	public Command build() {
		return new Command(uuid, method, payloadType, payload);
	}
}
