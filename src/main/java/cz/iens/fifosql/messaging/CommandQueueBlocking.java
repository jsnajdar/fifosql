package cz.iens.fifosql.messaging;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import cz.iens.fifosql.model.Command;

public class CommandQueueBlocking implements CommandQueue {
	
	private final BlockingQueue<Command> queue;
	
	public CommandQueueBlocking(int maxSize) {
		queue = new LinkedBlockingDeque<>(maxSize);
	}
	
	public void send(Command c) throws InterruptedException {
		queue.put(c);
	}

	public Command receive() throws InterruptedException {
		return queue.take();
	}
	
	public List<Command> getQueuedCommands() {
		return queue.stream().collect(Collectors.toList());
	}
	
}
