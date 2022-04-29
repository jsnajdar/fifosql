package cz.iens.fifosql.messaging;

import java.util.List;

import cz.iens.fifosql.model.Command;

public interface CommandQueue {
	
	public void send(Command c) throws InterruptedException;

	public Command receive() throws InterruptedException;
	
	public List<Command> getQueuedCommands();
	
}
