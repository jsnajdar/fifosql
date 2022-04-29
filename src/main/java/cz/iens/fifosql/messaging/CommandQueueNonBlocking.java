package cz.iens.fifosql.messaging;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import cz.iens.fifosql.model.Command;

public class CommandQueueNonBlocking implements CommandQueue {
	
	private final Queue<Command> queue;
	
	public CommandQueueNonBlocking() {
		queue = new ConcurrentLinkedQueue<>();
	}
	
	public void send(Command c) throws InterruptedException {
		queue.add(c);
	}

	public Command receive() throws InterruptedException {
		return queue.poll();
	}
	
	public List<Command> getQueuedCommands() {
		return queue.stream().collect(Collectors.toList());
	}
	
}
