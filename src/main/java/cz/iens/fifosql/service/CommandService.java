package cz.iens.fifosql.service;

import java.io.Serializable;

import cz.iens.fifosql.exception.CommandException;
import cz.iens.fifosql.model.Command;

public interface CommandService {
	
	void add(Serializable payload) throws CommandException;
	void printAll();
	void deleteAll();

	default void dispatch(Command c) throws CommandException {
		switch (c.getMethod()) {
			case ADD: add(c.getPayload());
				break;
			case PRINT_ALL: printAll(); 
				break;
			case DELETE_ALL: deleteAll(); 
				break;
			default:
				throw new CommandException("Unknown command method: " + c.getMethod());
		}
	}

	
}
