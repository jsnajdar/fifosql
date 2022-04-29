package cz.iens.fifosql.factory;

import cz.iens.fifosql.exception.CommandException;
import cz.iens.fifosql.model.Command;
import cz.iens.fifosql.service.CommandService;

public interface CommandServiceFactory {

	CommandService getCommandService(Command command) throws CommandException;
}
