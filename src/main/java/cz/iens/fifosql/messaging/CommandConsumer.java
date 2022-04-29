package cz.iens.fifosql.messaging;

import java.util.concurrent.atomic.AtomicBoolean;

import cz.iens.fifosql.exception.CommandException;
import cz.iens.fifosql.factory.CommandServiceFactory;
import cz.iens.fifosql.model.Command;
import cz.iens.fifosql.service.CommandService;

public class CommandConsumer implements ControlRunnable {

	private final AtomicBoolean running = new AtomicBoolean(false);
	private final CommandQueue queue;
	private final CommandServiceFactory commandServiceFactory;
	private int sleepAfterDispatch = 0;
	private int idleSleep = 0;
	
	public CommandConsumer(CommandQueue queue, CommandServiceFactory commandServiceFactory, 
			int idleSleep,
			int sleepAfterDispatch) {
		super();
		this.queue = queue; 
		this.commandServiceFactory = commandServiceFactory;
		this.sleepAfterDispatch = sleepAfterDispatch;
		this.idleSleep = idleSleep;
	}

	@Override
	public void run() {
		running.set(true);
		consume();
	}
	
	@Override
	public void stop() {
		running.set(false);
	}
	
	private void consume() {
		while (running.get()) {
			try {
				Command command = queue.receive();
				if (command == null) {
					System.out.println("Consumer " + Thread.currentThread().getName() + " is idle");
					Thread.sleep(idleSleep);
					continue;
				}
				System.out.println("Consumer " + Thread.currentThread().getName() + " receives: " + command);
				CommandService service = commandServiceFactory.getCommandService(command);
				service.dispatch(command);
				if (sleepAfterDispatch != 0) {
					Thread.sleep(sleepAfterDispatch);
				}
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName() + " interrupted");
				Thread.currentThread().interrupt();
			} catch (CommandException ec) {
				System.err.println(ec.getMessage());
			}
		}
	}

}
