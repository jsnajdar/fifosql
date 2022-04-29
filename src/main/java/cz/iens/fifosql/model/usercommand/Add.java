package cz.iens.fifosql.model.usercommand;

import cz.iens.fifosql.builder.CommandBuilder;
import cz.iens.fifosql.builder.UserBuilder;
import cz.iens.fifosql.model.Command;
import cz.iens.fifosql.model.CommandMethod;
import cz.iens.fifosql.model.CommandPayloadType;
import cz.iens.fifosql.model.User;

public class Add implements UserCommand {

	private final Command command;
	private final User user;
	
	public Add(Long id, String guid, String name) {
		super();
		this.user = new UserBuilder()
				.id(id)
				.guid(guid)
				.name(name)
				.build();
		this.command = new CommandBuilder()
			.method(CommandMethod.ADD)
			.payloadType(CommandPayloadType.USER)
			.payload(user)
			.build();
	}

	public Command getCommand() {
		return command;
	}

	public User getUser() {
		return user;
	}
}
