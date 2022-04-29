package cz.iens.fifosql.repository;

import java.util.List;

import cz.iens.fifosql.model.User;

public interface Repository {

	void create();
	void insert(User user);
	public List<User> selectAll();
	public void deleteAll();
	
}
