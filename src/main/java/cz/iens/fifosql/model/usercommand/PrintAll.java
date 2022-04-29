package cz.iens.fifosql.model.usercommand;

import cz.iens.fifosql.builder.CommandBuilder;
import cz.iens.fifosql.model.Command;
import cz.iens.fifosql.model.CommandMethod;
import cz.iens.fifosql.model.CommandPayloadType;

public class PrintAll implements UserCommand {

	private final Command command;
	
	public PrintAll() {
		super();
		this.command = new CommandBuilder()
			.method(CommandMethod.PRINT_ALL)
			.payloadType(CommandPayloadType.USER)			
			.build();
	}

	public Command getCommand() {
		return command;
	}
}
