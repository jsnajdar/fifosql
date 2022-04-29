package cz.iens.fifosql.messaging;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import cz.iens.fifosql.model.Command;

public class CommandProducer implements ControlRunnable {

	private final AtomicBoolean running = new AtomicBoolean(false);
	private final CommandQueue queue;
	private Queue<Command> inputCommands;
	private int sleepAfterSend = 0;
	private int idleSleep = 0;

	public CommandProducer(CommandQueue queue, List<Command> inputCommands, int idleSleep, int sleepAfterSend) {
		super();
		this.queue = queue; 
		this.inputCommands = new ConcurrentLinkedQueue<>();
		this.inputCommands.addAll(inputCommands);
		this.sleepAfterSend = sleepAfterSend;
		this.idleSleep = idleSleep;
	}

	@Override
	public void run() {
		running.set(true);
		produce();
	}
	
	@Override
	public void stop() {
		running.set(false);
	}
	
	public void send(Command command) {
		inputCommands.add(command);
	}
	
	private void produce() {
		while (running.get()) {
	        try {
				Command command = inputCommands.poll();
				if (command == null) {
					System.out.println("Producer " + Thread.currentThread().getName() + " is idle");
					Thread.sleep(idleSleep);
					continue;
				}
	        	queue.send(command);
				System.out.println("Producer " + Thread.currentThread().getName() + " sends: " + command);
				if (sleepAfterSend != 0) {
					Thread.sleep(sleepAfterSend);
				}
	        } catch (InterruptedException e) {
	        	System.err.println(Thread.currentThread().getName() + " interrupted");
	        	Thread.currentThread().interrupt();
	        }
		}
	}
	
}
