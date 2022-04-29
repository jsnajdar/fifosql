package cz.iens.fifosql.factory;

import cz.iens.fifosql.database.JdbcDatabase;
import cz.iens.fifosql.exception.CommandException;
import cz.iens.fifosql.model.Command;
import cz.iens.fifosql.model.CommandPayloadType;
import cz.iens.fifosql.repository.UserJdbcRepository;
import cz.iens.fifosql.service.CommandService;
import cz.iens.fifosql.service.UserCommandService;

public class CommandServiceJdbcRepositoryFactory implements CommandServiceFactory {

	private JdbcDatabase database;
	
	public CommandServiceJdbcRepositoryFactory(JdbcDatabase database) {
		super();
		this.database = database;
	}

	public CommandService getCommandService(Command command) throws CommandException {
		if (command == null) {
			throw new CommandException("Cannot determine CommandService for null command");			
		}
		if (command.getPayloadType() == CommandPayloadType.USER) {
			return new UserCommandService(new UserJdbcRepository(database));
		}
		throw new CommandException("Unknown payload type: " + command.getPayloadType());
	}
	
}
