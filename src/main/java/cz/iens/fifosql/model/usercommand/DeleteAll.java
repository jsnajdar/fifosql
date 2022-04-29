package cz.iens.fifosql.model.usercommand;

import cz.iens.fifosql.builder.CommandBuilder;
import cz.iens.fifosql.model.Command;
import cz.iens.fifosql.model.CommandMethod;
import cz.iens.fifosql.model.CommandPayloadType;

public class DeleteAll implements UserCommand {

	private final Command command;
	
	public DeleteAll() {
		super();
		this.command = new CommandBuilder()
			.method(CommandMethod.DELETE_ALL)
			.payloadType(CommandPayloadType.USER)
			.build();
	}

	public Command getCommand() {
		return command;
	}
}
