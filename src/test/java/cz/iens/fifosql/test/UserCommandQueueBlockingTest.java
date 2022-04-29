package cz.iens.fifosql.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

import cz.iens.fifosql.database.EmbeddedH2Database;
import cz.iens.fifosql.factory.CommandServiceFactory;
import cz.iens.fifosql.factory.CommandServiceJdbcRepositoryFactory;
import cz.iens.fifosql.messaging.*;
import cz.iens.fifosql.model.Command;
import cz.iens.fifosql.model.User;
import cz.iens.fifosql.model.usercommand.Add;
import cz.iens.fifosql.model.usercommand.DeleteAll;
import cz.iens.fifosql.model.usercommand.PrintAll;
import cz.iens.fifosql.model.usercommand.UserCommand;
import cz.iens.fifosql.repository.Repository;
import cz.iens.fifosql.repository.UserJdbcRepository;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserCommandQueueBlockingTest {

	private static final int IDLE_SLEEP = 100;
	
	Repository repository;
	List<Command> defaultCommands;
	CommandQueue queue;
	CommandServiceFactory serviceFactory;
	
	@BeforeAll
	public static void initAll() {
		UserJdbcRepository rep = new UserJdbcRepository(EmbeddedH2Database.INSTANCE);
		rep.drop();
		rep.create();
	}
	
	@BeforeEach 
	void initEach() {
		repository = new UserJdbcRepository(EmbeddedH2Database.INSTANCE);
		repository.deleteAll();
		defaultCommands = List.of(
			new Add(1L, "a1", "Robert"),
			new Add(2L, "a2", "Martin"),
			new PrintAll(),
			new DeleteAll(),
			new PrintAll()
		).stream().map(UserCommand::getCommand).collect(Collectors.toList());
		queue = new CommandQueueBlocking(10);
		serviceFactory = new CommandServiceJdbcRepositoryFactory(EmbeddedH2Database.INSTANCE);
	}
		
	private void start(List<ControlRunnable> runnables) {
		runnables.forEach(runnable -> {
			Thread t = new Thread(runnable);
			System.out.println(t.getName() + " starting ");
			t.start();
		});
	}
	private void stop(List<ControlRunnable> runnables) {
		runnables.forEach(ControlRunnable::stop);
	}
	
	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void startDelayStop(List<ControlRunnable> runnables, int sleepMs) {
		start(runnables);
		sleep(sleepMs);
		stop(runnables);
	}

	@Test 
	@Order(0)
	void testOneProducerOneConsumerSendingPerOneCommand() {
		System.out.println("************** testOneProducerOneConsumerSendingPerOneCommand *******************");
		
		// starting with empty list of commands, sleeping after send 100ms
		CommandProducer producer = new CommandProducer(queue, new ArrayList<>(), IDLE_SLEEP, 100);
		// consumer consumes immediatelly
		CommandConsumer consumer = new CommandConsumer(queue, serviceFactory, IDLE_SLEEP, 0);

		// start threads for producer and consumer
		start(List.of(producer, consumer));

		producer.send(defaultCommands.get(0));
		producer.send(defaultCommands.get(1));
		producer.send(defaultCommands.get(2));
		producer.send(defaultCommands.get(3));
		producer.send(defaultCommands.get(4));
		
		sleep(2000);
		
		stop(List.of(producer, consumer));
		
		List<Command> currentQueueCommands = queue.getQueuedCommands();
		assertEquals(0, currentQueueCommands.size());
		
		List<User> users = repository.selectAll();
		assertEquals(0, users.size());
	}
	
	@Test 
	@Order(1)
	void testOneProducerOneConsumerSendingWithList() {
		System.out.println("************** testOneProducerOneConsumerSendingWithList *******************");
		List<ControlRunnable> runnables = List.of(
			// no waiting while sending commands
			new CommandProducer(queue, defaultCommands, IDLE_SLEEP, 0),
			// no waiting while receiving commands
			new CommandConsumer(queue, serviceFactory, IDLE_SLEEP, 0)
		);
		
		// waiting is enough to process all commands
		startDelayStop(runnables, 2000);
		
		List<Command> currentQueueCommands = queue.getQueuedCommands();
		assertEquals(0, currentQueueCommands.size());
		
		List<User> users = repository.selectAll();
		assertEquals(0, users.size());
	}

	@Test 
	@Order(2)
	void testOneProducerTwoConsumersSendingWithList() {
		System.out.println("************** testOneProducerTwoConsumersSendingWithList *******************");
		List<ControlRunnable> runnables = List.of(
			new CommandProducer(queue, defaultCommands, IDLE_SLEEP, 0), 
			new CommandConsumer(queue, serviceFactory, IDLE_SLEEP, 100),
			new CommandConsumer(queue, serviceFactory, IDLE_SLEEP, 100)
		);

		// waiting is enough to process all commands
		startDelayStop(runnables, 2000);

		List<Command> currentQueueCommands = queue.getQueuedCommands();
		assertEquals(0, currentQueueCommands.size());
		List<User> users = repository.selectAll();
		assertEquals(0, users.size());
		
	}

	@Test 
	@Order(3)
	void testTwoProducersTwoConsumersSendingWithListSecondUserRemains() {
		System.out.println("************** testTwoProducersTwoConsumersSendingWithListSecondUserRemains *******************");
		List<ControlRunnable> runnables = List.of(
			// add both users, but big pauses
			new CommandProducer(queue, defaultCommands.subList(0, 2), IDLE_SLEEP, 1200),
			// print, delete, print, but small pauses
			new CommandProducer(queue, defaultCommands.subList(2, 5), IDLE_SLEEP, 200),

			// consumers receives as soon as possible
			new CommandConsumer(queue, serviceFactory, IDLE_SLEEP, 100),
			new CommandConsumer(queue, serviceFactory, IDLE_SLEEP, 100)
		);

		// waiting is enough to process all commands
		startDelayStop(runnables, 3000);
				
		// all commands were consumed
		List<Command> currentQueueCommands = queue.getQueuedCommands();
		assertEquals(0, currentQueueCommands.size());
		
		// second user remains in database
		User u2 = (User) defaultCommands.get(1).getPayload();
		List<User> users = repository.selectAll();
		assertThat(users).contains(u2);
	}

	@Test 
	@Order(4)
	void testOneProducerOneConsumerSendingWithListBothUsersRemains() {
		System.out.println("************** testOneProducerOneConsumerSendingWithListBothUsersRemains *******************");
		List<ControlRunnable> runnables = List.of(
			new CommandProducer(queue, defaultCommands, IDLE_SLEEP, 0),
			// each receive is delayed
			new CommandConsumer(queue, serviceFactory, IDLE_SLEEP, 800)
		);

		// waiting will not be enough to receive all commands
		startDelayStop(runnables, 1000);
		
		List<Command> currentQueueCommands = queue.getQueuedCommands();
		assertEquals(3, currentQueueCommands.size());
		
		List<User> users = repository.selectAll();
		assertEquals(2, users.size());
	}
	

	@Test 
	@Order(5)
	void testOneProducerOneConsumerSendingWithListQueueSizeOne() {
		// maxsize 1 will cause waiting of producer for emptying queue due to delayed consumer
		queue = new CommandQueueBlocking(1);
		System.out.println("************** testOneProducerOneConsumerSendingWithListQueueSizeOne *******************");
		List<ControlRunnable> runnables = List.of(
			new CommandProducer(queue, defaultCommands, IDLE_SLEEP, 0),
			// consumer is very delayed
			new CommandConsumer(queue, serviceFactory, IDLE_SLEEP, 900)
		);

		// waiting will not be enough to receive all commands
		startDelayStop(runnables, 1000);
		
		List<Command> currentQueueCommands = queue.getQueuedCommands();
		// only one command in queue
		assertEquals(1, currentQueueCommands.size());
		
		// only first user was saved
		User u1 = (User) defaultCommands.get(0).getPayload();
		List<User> users = repository.selectAll();
		assertThat(users).contains(u1);
	}
	
	
}
