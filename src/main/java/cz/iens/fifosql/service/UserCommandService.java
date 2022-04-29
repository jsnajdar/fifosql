package cz.iens.fifosql.service;

import java.io.Serializable;
import java.util.List;

import cz.iens.fifosql.exception.CommandException;
import cz.iens.fifosql.model.User;
import cz.iens.fifosql.repository.Repository;

public class UserCommandService implements CommandService {

	Repository repository;
	
	public UserCommandService(Repository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void add(Serializable payload) throws CommandException {
		if (!(payload instanceof User)) {
			throw new CommandException("Expected User class, but: " + payload.getClass().getCanonicalName());
		}		
		add((User) payload);
	}
	
	public void add(User user) {
		log(" add " + user);
		repository.insert(user);
		
	}

	@Override
	public void deleteAll() {
		log(" deleteAll ");
		repository.deleteAll();
	}

	@Override
	public void printAll() {
		log(" printAll ");
		List<User> users = repository.selectAll();
		if (users.isEmpty()) {
			log(" none");
		} else {
			users.forEach(user -> log(" " + user));
		}
	}

	private void log(String msg) {
		System.out.println("UserCommandService "  + Thread.currentThread().getName() + msg);
	}
	
}
